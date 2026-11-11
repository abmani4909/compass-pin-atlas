package com.meridian.compasspin.data.repository

import com.meridian.compasspin.data.local.AtlasPreferences
import com.meridian.compasspin.domain.model.AppSettings
import com.meridian.compasspin.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val preferences: AtlasPreferences) : SettingsRepository {

    private val _settings = MutableStateFlow(read())
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun setUseMiles(value: Boolean) {
        preferences.useMiles = value
        _settings.value = read()
    }

    override fun setHardBorders(value: Boolean) {
        preferences.hardBorders = value
        _settings.value = read()
    }

    override fun setSoundEnabled(value: Boolean) {
        preferences.soundEnabled = value
        _settings.value = read()
    }

    private fun read(): AppSettings = AppSettings(
        useMiles = preferences.useMiles,
        hardBorders = preferences.hardBorders,
        soundEnabled = preferences.soundEnabled
    )
}
