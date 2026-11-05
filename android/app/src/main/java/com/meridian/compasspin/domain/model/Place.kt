package com.meridian.compasspin.domain.model

data class Place(
    val id: String,
    val name: String,
    val category: Category,
    val lat: Double,
    val lon: Double,
    val region: String,
    val fact: String
) {
    val position: LatLon get() = LatLon(lat, lon)
}
