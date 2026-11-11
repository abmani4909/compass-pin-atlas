package com.meridian.compasspin.domain.model

data class WeakSpot(
    val placeId: String,
    val placeName: String,
    val attempts: Int,
    val averageKm: Int
)
