package com.meridian.compasspin.domain.usecase

import com.meridian.compasspin.domain.model.LatLon
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class CalculateDistanceUseCase(private val earthRadiusKm: Double) {

    operator fun invoke(from: LatLon, to: LatLon): Double {
        val phi1 = Math.toRadians(from.lat)
        val phi2 = Math.toRadians(to.lat)
        val dPhi = Math.toRadians(to.lat - from.lat)
        val dLambda = Math.toRadians(to.lon - from.lon)
        val sinLat = sin(dPhi / 2.0)
        val sinLon = sin(dLambda / 2.0)
        val h = sinLat * sinLat + cos(phi1) * cos(phi2) * sinLon * sinLon
        return 2.0 * earthRadiusKm * asin(min(1.0, sqrt(h)))
    }
}
