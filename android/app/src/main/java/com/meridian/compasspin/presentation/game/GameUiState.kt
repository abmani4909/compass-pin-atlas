package com.meridian.compasspin.presentation.game

import com.meridian.compasspin.domain.model.ClueResult
import com.meridian.compasspin.domain.model.LatLon
import com.meridian.compasspin.domain.model.MapCamera
import com.meridian.compasspin.domain.model.Place

enum class GamePhase { GUESSING, PINNED, REVEALING, REVEALED }

data class GameUiState(
    val atlasId: String = "",
    val clueIndex: Int = 0,
    val totalClues: Int = 0,
    val place: Place? = null,
    val phase: GamePhase = GamePhase.GUESSING,
    val camera: MapCamera = MapCamera(),
    val pin: LatLon? = null,
    val truth: LatLon? = null,
    val arcFraction: Float = 0f,
    val distanceKm: Int = 0,
    val pointsEarned: Int = 0,
    val bullseye: Boolean = false,
    val score: Int = 0,
    val results: List<ClueResult> = emptyList(),
    val showBorders: Boolean = true,
    val useMiles: Boolean = false,
    val abandonVisible: Boolean = false
) {
    val lastClue: Boolean get() = clueIndex >= totalClues - 1
    val pinPlaced: Boolean get() = pin != null
    val interactive: Boolean get() = phase == GamePhase.GUESSING || phase == GamePhase.PINNED
}
