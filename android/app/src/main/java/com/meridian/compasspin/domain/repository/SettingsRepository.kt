package com.meridian.compasspin.domain.repository

import com.meridian.compasspin.domain.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>
    fun setUseMiles(value: Boolean)
    fun setHardBorders(value: Boolean)
    fun setSoundEnabled(value: Boolean)
}
