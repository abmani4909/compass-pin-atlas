package com.meridian.compasspin.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.repository.AtlasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(atlasRepository: AtlasRepository) : ViewModel() {

    val state: StateFlow<MenuUiState> = atlasRepository.progress
        .map { progress ->
            MenuUiState(
                stampsEarned = progress.stampsEarned,
                totalStamps = SampleData.atlases.size,
                tripsCompleted = progress.tripsCompleted,
                bestGrade = progress.bestGrade,
                shelf = SampleData.regionalAtlases.map { atlas ->
                    AtlasEntry(atlas = atlas, grade = progress.grades[atlas.id])
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MenuUiState(
                totalStamps = SampleData.atlases.size,
                shelf = SampleData.regionalAtlases.map { AtlasEntry(it, null) }
            )
        )
}
