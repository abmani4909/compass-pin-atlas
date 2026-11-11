package com.meridian.compasspin.domain.repository

import com.meridian.compasspin.domain.model.AtlasProgress
import com.meridian.compasspin.domain.model.TripResult
import kotlinx.coroutines.flow.StateFlow

interface AtlasRepository {
    val progress: StateFlow<AtlasProgress>
    fun recordTrip(result: TripResult)
    fun clearProgress()
}
