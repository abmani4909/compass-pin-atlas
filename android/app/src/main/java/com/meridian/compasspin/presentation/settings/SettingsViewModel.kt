package com.meridian.compasspin.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridian.compasspin.domain.model.AppSettings
import com.meridian.compasspin.domain.repository.AtlasRepository
import com.meridian.compasspin.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val atlasRepository: AtlasRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = settingsRepository.settings.value
        )

    private val _resetVisible = MutableStateFlow(false)
    val resetVisible: StateFlow<Boolean> = _resetVisible.asStateFlow()

    fun onUnitSelected(useMiles: Boolean) = settingsRepository.setUseMiles(useMiles)

    fun onBordersSelected(hard: Boolean) = settingsRepository.setHardBorders(hard)

    fun onSoundToggled(enabled: Boolean) = settingsRepository.setSoundEnabled(enabled)

    fun onResetRequested() {
        _resetVisible.value = true
    }

    fun onResetDismissed() {
        _resetVisible.value = false
    }

    fun onResetConfirmed() {
        atlasRepository.clearProgress()
        _resetVisible.value = false
    }
}
