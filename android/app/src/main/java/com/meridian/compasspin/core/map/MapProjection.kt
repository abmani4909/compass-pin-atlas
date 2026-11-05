package com.meridian.compasspin.core.map

import com.meridian.compasspin.core.config.GameConfig
import com.meridian.compasspin.domain.model.LatLon
import com.meridian.compasspin.domain.model.MapCamera
import kotlin.math.abs
import kotlin.math.max

object MapProjection {

    fun sheetWidth(viewWidth: Float, scale: Float): Float = viewWidth * scale

    fun sheetHeight(viewWidth: Float, scale: Float): Float = viewWidth * scale / 2f

    fun screenX(lon: Double, viewWidth: Float, camera: MapCamera): Float {
        val sheet = sheetWidth(viewWidth, camera.scale)
        return viewWidth / 2f + ((lon - camera.centerLon) / 360.0).toFloat() * sheet
    }

    fun screenY(lat: Double, viewWidth: Float, viewHeight: Float, camera: MapCamera): Float {
        val sheet = sheetHeight(viewWidth, camera.scale)
        return viewHeight / 2f - ((lat - camera.centerLat) / 180.0).toFloat() * sheet
    }

    fun toLatLon(x: Float, y: Float, viewWidth: Float, viewHeight: Float, camera: MapCamera): LatLon {
        val sheetW = sheetWidth(viewWidth, camera.scale)
        val sheetH = sheetHeight(viewWidth, camera.scale)
        val lon = camera.centerLon + (x - viewWidth / 2f) / sheetW * 360.0
        val lat = camera.centerLat - (y - viewHeight / 2f) / sheetH * 180.0
        return LatLon(lat.coerceIn(-89.9, 89.9), wrapLon(lon))
    }

    fun pan(camera: MapCamera, dx: Float, dy: Float, viewWidth: Float, viewHeight: Float): MapCamera {
        val sheetW = sheetWidth(viewWidth, camera.scale)
        val sheetH = sheetHeight(viewWidth, camera.scale)
        if (sheetW <= 0f || sheetH <= 0f) return camera
        val moved = camera.copy(
            centerLon = camera.centerLon - dx / sheetW * 360.0,
            centerLat = camera.centerLat + dy / sheetH * 180.0
        )
        return clamp(moved, viewWidth, viewHeight)
    }

    fun zoom(camera: MapCamera, factor: Float, viewWidth: Float, viewHeight: Float): MapCamera {
        val scale = (camera.scale * factor).coerceIn(GameConfig.MIN_SCALE, GameConfig.MAX_SCALE)
        return clamp(camera.copy(scale = scale), viewWidth, viewHeight)
    }

    fun clamp(camera: MapCamera, viewWidth: Float, viewHeight: Float): MapCamera {
        if (viewWidth <= 0f || viewHeight <= 0f) return camera
        val sheetW = sheetWidth(viewWidth, camera.scale)
        val sheetH = sheetHeight(viewWidth, camera.scale)
        val halfLon = 180.0 * (viewWidth / sheetW)
        val halfLat = 90.0 * (viewHeight / sheetH)
        val lonLimit = (180.0 - halfLon).coerceAtLeast(0.0)
        val latLimit = (90.0 - halfLat * 0.35).coerceIn(0.0, 80.0)
        return camera.copy(
            centerLon = camera.centerLon.coerceIn(-lonLimit, lonLimit),
            centerLat = camera.centerLat.coerceIn(-latLimit, latLimit)
        )
    }

    fun frame(a: LatLon, b: LatLon, viewWidth: Float, viewHeight: Float): MapCamera {
        val lonSpan = max(abs(a.lon - b.lon) * 1.7 + 24.0, 30.0)
        val latSpan = max(abs(a.lat - b.lat) * 1.7 + 16.0, 18.0)
        val byLon = (360.0 / lonSpan).toFloat()
        val byLat = (180.0 / latSpan).toFloat()
        val scale = minOf(byLon, byLat).coerceIn(GameConfig.MIN_SCALE, GameConfig.MAX_SCALE)
        val camera = MapCamera(
            scale = scale,
            centerLon = (a.lon + b.lon) / 2.0,
            centerLat = (a.lat + b.lat) / 2.0
        )
        return clamp(camera, viewWidth, viewHeight)
    }

    private fun wrapLon(lon: Double): Double {
        var value = lon
        while (value > 180.0) value -= 360.0
        while (value < -180.0) value += 360.0
        return value
    }
}
