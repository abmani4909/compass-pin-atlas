package com.meridian.compasspin.presentation.passport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.model.Grade
import com.meridian.compasspin.domain.repository.AtlasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class StampEntry(
    val atlasId: String,
    val grade: Grade?,
    val score: Int
)

data class PassportUiState(
    val stamps: List<StampEntry> = emptyList(),
    val earned: Int = 0,
    val total: Int = 0
)

class PassportViewModel(atlasRepository: AtlasRepository) : ViewModel() {

    val state: StateFlow<PassportUiState> = atlasRepository.progress
        .map { progress ->
            val stamps = SampleData.atlases.map { atlas ->
                StampEntry(
                    atlasId = atlas.id,
                    grade = progress.grades[atlas.id],
                    score = progress.bestScores[atlas.id] ?: 0
                )
            }
            PassportUiState(
                stamps = stamps,
                earned = stamps.count { it.grade != null },
                total = stamps.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PassportUiState(
                stamps = SampleData.atlases.map { StampEntry(it.id, null, 0) },
                earned = 0,
                total = SampleData.atlases.size
            )
        )
}
