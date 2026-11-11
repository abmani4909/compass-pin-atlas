package com.meridian.compasspin.domain.model

data class AppSettings(
    val useMiles: Boolean = false,
    val hardBorders: Boolean = false,
    val soundEnabled: Boolean = true
)
