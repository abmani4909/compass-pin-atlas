package com.meridian.compasspin.domain.model

data class Atlas(
    val id: String,
    val regional: Boolean,
    val scale: Float,
    val centerLon: Double,
    val centerLat: Double
)
