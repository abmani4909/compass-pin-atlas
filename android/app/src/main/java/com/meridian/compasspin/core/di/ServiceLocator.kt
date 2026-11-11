package com.meridian.compasspin.core.di

import android.content.Context
import com.meridian.compasspin.core.config.GameConfig
import com.meridian.compasspin.data.local.AtlasPreferences
import com.meridian.compasspin.data.repository.AtlasRepositoryImpl
import com.meridian.compasspin.data.repository.SettingsRepositoryImpl
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.repository.AtlasRepository
import com.meridian.compasspin.domain.repository.SettingsRepository
import com.meridian.compasspin.domain.usecase.BuildTripUseCase
import com.meridian.compasspin.domain.usecase.CalculateDistanceUseCase
import com.meridian.compasspin.domain.usecase.GradeTripUseCase
import com.meridian.compasspin.domain.usecase.ScoreGuessUseCase

object ServiceLocator {

    private var preferences: AtlasPreferences? = null
    private var atlas: AtlasRepository? = null
    private var settings: SettingsRepository? = null

    val calculateDistance: CalculateDistanceUseCase = CalculateDistanceUseCase(GameConfig.EARTH_RADIUS_KM)
    val scoreGuess: ScoreGuessUseCase = ScoreGuessUseCase()
    val gradeTrip: GradeTripUseCase = GradeTripUseCase()
    val buildTrip: BuildTripUseCase = BuildTripUseCase(SampleData.places)

    fun initialize(context: Context) {
        if (preferences != null) return
        val store = AtlasPreferences(context.applicationContext)
        preferences = store
        atlas = AtlasRepositoryImpl(store)
        settings = SettingsRepositoryImpl(store)
    }

    val atlasRepository: AtlasRepository
        get() = requireNotNull(atlas)

    val settingsRepository: SettingsRepository
        get() = requireNotNull(settings)
}
