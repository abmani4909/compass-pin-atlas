package com.meridian.compasspin.domain.usecase

import kotlin.math.exp
import kotlin.math.roundToInt

class ScoreGuessUseCase {

    operator fun invoke(distanceKm: Double, falloffKm: Double, bullseyeKm: Double): Int {
        if (distanceKm <= bullseyeKm) return 1000
        return (1000.0 * exp(-distanceKm / falloffKm)).roundToInt().coerceIn(0, 1000)
    }
}
