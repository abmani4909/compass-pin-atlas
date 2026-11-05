package com.meridian.compasspin.presentation.gameover

import androidx.lifecycle.ViewModel
import com.meridian.compasspin.domain.model.TripResult
import com.meridian.compasspin.domain.repository.AtlasRepository
import com.meridian.compasspin.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameOverUiState(
    val trip: TripResult,
    val useMiles: Boolean,
    val stampsEarned: Int,
    val newRecord: Boolean
)

class GameOverViewModel(
    trip: TripResult,
    atlasRepository: AtlasRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<GameOverUiState>
    val state: StateFlow<GameOverUiState>

    init {
        val previous = atlasRepository.progress.value.grades[trip.atlasId]
        val improved = previous == null || trip.grade.ordinal < previous.ordinal
        atlasRepository.recordTrip(trip)
        _state = MutableStateFlow(
            GameOverUiState(
                trip = trip,
                useMiles = settingsRepository.settings.value.useMiles,
                stampsEarned = atlasRepository.progress.value.stampsEarned,
                newRecord = improved
            )
        )
        state = _state.asStateFlow()
    }
}
