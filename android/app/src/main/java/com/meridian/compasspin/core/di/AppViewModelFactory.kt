package com.meridian.compasspin.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.model.TripResult
import com.meridian.compasspin.presentation.app.AppViewModel
import com.meridian.compasspin.presentation.game.GameViewModel
import com.meridian.compasspin.presentation.gameover.GameOverViewModel
import com.meridian.compasspin.presentation.menu.MenuViewModel
import com.meridian.compasspin.presentation.passport.PassportViewModel
import com.meridian.compasspin.presentation.settings.SettingsViewModel
import com.meridian.compasspin.presentation.splash.SplashViewModel
import com.meridian.compasspin.presentation.study.StudyViewModel
import com.meridian.compasspin.presentation.weakspots.WeakSpotsViewModel

class AppViewModelFactory(
    private val atlasId: String = SampleData.WORLD,
    private val trip: TripResult? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(ServiceLocator.atlasRepository)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    atlasId = atlasId,
                    settingsRepository = ServiceLocator.settingsRepository,
                    buildTrip = ServiceLocator.buildTrip,
                    calculateDistance = ServiceLocator.calculateDistance,
                    scoreGuess = ServiceLocator.scoreGuess,
                    gradeTrip = ServiceLocator.gradeTrip
                )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(
                    trip = requireNotNull(trip),
                    atlasRepository = ServiceLocator.atlasRepository,
                    settingsRepository = ServiceLocator.settingsRepository
                )
            modelClass.isAssignableFrom(WeakSpotsViewModel::class.java) ->
                WeakSpotsViewModel(ServiceLocator.atlasRepository)
            modelClass.isAssignableFrom(StudyViewModel::class.java) -> StudyViewModel()
            modelClass.isAssignableFrom(PassportViewModel::class.java) ->
                PassportViewModel(ServiceLocator.atlasRepository)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(ServiceLocator.settingsRepository, ServiceLocator.atlasRepository)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
