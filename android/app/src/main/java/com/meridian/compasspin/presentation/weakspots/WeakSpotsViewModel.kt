package com.meridian.compasspin.presentation.weakspots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridian.compasspin.domain.model.WeakSpot
import com.meridian.compasspin.domain.repository.AtlasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class WeakSpotsUiState(
    val spots: List<WeakSpot> = emptyList(),
    val tripsCompleted: Int = 0,
    val worstKm: Int = 0
)

class WeakSpotsViewModel(atlasRepository: AtlasRepository) : ViewModel() {

    val state: StateFlow<WeakSpotsUiState> = atlasRepository.progress
        .map { progress ->
            val spots = progress.weakSpots.take(LIMIT)
            WeakSpotsUiState(
                spots = spots,
                tripsCompleted = progress.tripsCompleted,
                worstKm = spots.firstOrNull()?.averageKm ?: 0
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WeakSpotsUiState()
        )

    private companion object {
        const val LIMIT = 8
    }
}
