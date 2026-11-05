package com.meridian.compasspin.domain.model

data class ClueResult(
    val placeId: String,
    val placeName: String,
    val category: Category,
    val distanceKm: Int,
    val points: Int,
    val fact: String,
    val bullseye: Boolean
)
