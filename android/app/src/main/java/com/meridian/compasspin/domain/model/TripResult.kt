package com.meridian.compasspin.domain.model

data class TripResult(
    val tripId: Long,
    val atlasId: String,
    val totalScore: Int,
    val grade: Grade,
    val clues: List<ClueResult>
) {
    val bestClueKm: Int get() = clues.minOfOrNull { it.distanceKm } ?: 0
    val averageOffKm: Int get() = if (clues.isEmpty()) 0 else clues.sumOf { it.distanceKm } / clues.size
}
