package com.meridian.compasspin.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.meridian.compasspin.core.map.MapProjection
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.theme.LandStroke
import com.meridian.compasspin.core.theme.Paper
import com.meridian.compasspin.data.sample.MapGeometry
import com.meridian.compasspin.domain.model.LatLon
import com.meridian.compasspin.domain.model.MapCamera

@Composable
fun WorldMapCanvas(
    camera: MapCamera,
    pin: LatLon?,
    truth: LatLon?,
    arcFraction: Float,
    showBorders: Boolean,
    interactive: Boolean,
    mapLabel: String,
    onTransform: (Float, Float, Float, Float, Float) -> Unit,
    onTap: (Float, Float, Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val liftPx = with(density) { 44.dp.toPx() }
    val pinDrop by animateFloatAsState(
        targetValue = if (pin == null) 0.55f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = 700f)
    )

    Box(
        modifier = modifier
            .semantics { contentDescription = mapLabel }
            .pointerInput(interactive) {
                if (!interactive) return@pointerInput
                detectTapGestures { offset ->
                    onTap(offset.x, offset.y - liftPx, size.width.toFloat(), size.height.toFloat())
                }
            }
            .pointerInput(interactive) {
                if (!interactive) return@pointerInput
                detectTransformGestures { _, pan, zoom, _ ->
                    onTransform(pan.x, pan.y, zoom, size.width.toFloat(), size.height.toFloat())
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawRect(color = Paper, size = size)
            drawGraticule(w, h, camera)
            if (showBorders) {
                MapGeometry.borders.forEach { line ->
                    drawPolyline(line, w, h, camera, Hairline.copy(alpha = 0.75f), 1.2f)
                }
            }
            MapGeometry.landmasses.forEach { outline ->
                val path = buildPath(outline, w, h, camera, true)
                drawPath(path = path, color = Land)
                drawPath(
                    path = path,
                    color = LandStroke,
                    style = Stroke(width = 2.2f, pathEffect = PathEffect.cornerPathEffect(2f))
                )
            }
            if (pin != null && truth != null && arcFraction > 0f) {
                drawArcBetween(pin, truth, arcFraction, w, h, camera)
            }
            if (truth != null) {
                drawTruthMarker(truth, w, h, camera)
            }
            if (pin != null) {
                drawPin(pin, w, h, camera, pinDrop)
            }
        }
    }
}

private fun DrawScope.drawGraticule(w: Float, h: Float, camera: MapCamera) {
    var lon = -180.0
    while (lon <= 180.0) {
        val x = MapProjection.screenX(lon, w, camera)
        if (x > -10f && x < w + 10f) {
            drawLine(
                color = Hairline.copy(alpha = 0.55f),
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1f
            )
        }
        lon += 20.0
    }
    var lat = -80.0
    while (lat <= 80.0) {
        val y = MapProjection.screenY(lat, w, h, camera)
        if (y > -10f && y < h + 10f) {
            drawLine(
                color = Hairline.copy(alpha = if (lat == 0.0) 0.9f else 0.55f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = if (lat == 0.0) 1.6f else 1f
            )
        }
        lat += 20.0
    }
}

private fun buildPath(
    outline: List<LatLon>,
    w: Float,
    h: Float,
    camera: MapCamera,
    close: Boolean
): Path {
    val path = Path()
    outline.forEachIndexed { index, point ->
        val x = MapProjection.screenX(point.lon, w, camera)
        val y = MapProjection.screenY(point.lat, w, h, camera)
        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    if (close) path.close()
    return path
}

private fun DrawScope.drawPolyline(
    line: List<LatLon>,
    w: Float,
    h: Float,
    camera: MapCamera,
    color: Color,
    width: Float
) {
    drawPath(
        path = buildPath(line, w, h, camera, false),
        color = color,
        style = Stroke(width = width)
    )
}

private fun DrawScope.drawArcBetween(
    from: LatLon,
    to: LatLon,
    fraction: Float,
    w: Float,
    h: Float,
    camera: MapCamera
) {
    val x0 = MapProjection.screenX(from.lon, w, camera)
    val y0 = MapProjection.screenY(from.lat, w, h, camera)
    val x1 = MapProjection.screenX(to.lon, w, camera)
    val y1 = MapProjection.screenY(to.lat, w, h, camera)
    val midX = (x0 + x1) / 2f
    val midY = (y0 + y1) / 2f
    val dx = x1 - x0
    val dy = y1 - y0
    val controlX = midX - dy * 0.22f
    val controlY = midY + dx * 0.22f
    val full = Path()
    full.moveTo(x0, y0)
    full.quadraticTo(controlX, controlY, x1, y1)
    val measure = PathMeasure()
    measure.setPath(full, false)
    val visible = Path()
    measure.getSegment(0f, measure.length * fraction.coerceIn(0f, 1f), visible, true)
    drawPath(
        path = visible,
        color = Accent,
        style = Stroke(
            width = 6f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
        )
    )
}

private fun DrawScope.drawTruthMarker(truth: LatLon, w: Float, h: Float, camera: MapCamera) {
    val x = MapProjection.screenX(truth.lon, w, camera)
    val y = MapProjection.screenY(truth.lat, w, h, camera)
    drawCircle(color = LandStroke, radius = 20f, center = Offset(x, y), style = Stroke(width = 5f))
    drawCircle(color = LandStroke, radius = 7f, center = Offset(x, y))
}

private fun DrawScope.drawPin(pin: LatLon, w: Float, h: Float, camera: MapCamera, drop: Float) {
    val x = MapProjection.screenX(pin.lon, w, camera)
    val tipY = MapProjection.screenY(pin.lat, w, h, camera)
    val radius = 26f * drop
    val headY = tipY - 68f * drop
    val body = Path()
    body.moveTo(x, tipY)
    body.lineTo(x - radius * 0.78f, headY + radius * 0.5f)
    body.lineTo(x + radius * 0.78f, headY + radius * 0.5f)
    body.close()
    drawPath(path = body, color = Accent)
    drawCircle(color = Accent, radius = radius, center = Offset(x, headY))
    drawCircle(color = Paper, radius = radius * 0.42f, center = Offset(x, headY))
    drawCircle(
        color = Accent.copy(alpha = (1f - drop).coerceIn(0f, 0.5f)),
        radius = radius * 2.4f,
        center = Offset(x, headY),
        style = Stroke(width = 4f)
    )
}
