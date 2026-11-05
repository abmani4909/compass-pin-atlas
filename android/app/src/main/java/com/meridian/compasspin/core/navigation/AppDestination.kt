package com.meridian.compasspin.core.navigation

import com.meridian.compasspin.domain.model.TripResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val atlasId: String, val session: Long) : Screen
    data class GameOver(val result: TripResult) : Screen
    data object Passport : Screen
    data object WeakSpots : Screen
    data object Study : Screen
    data object HowToPlay : Screen
    data object Settings : Screen
}
