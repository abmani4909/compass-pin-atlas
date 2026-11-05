package com.meridian.compasspin.presentation.menu

import com.meridian.compasspin.domain.model.Atlas
import com.meridian.compasspin.domain.model.Grade

data class AtlasEntry(
    val atlas: Atlas,
    val grade: Grade?
)

data class MenuUiState(
    val stampsEarned: Int = 0,
    val totalStamps: Int = 0,
    val tripsCompleted: Int = 0,
    val bestGrade: Grade? = null,
    val shelf: List<AtlasEntry> = emptyList()
)
