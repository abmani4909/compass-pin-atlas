package com.meridian.compasspin.domain.model

data class AtlasProgress(
    val grades: Map<String, Grade> = emptyMap(),
    val bestScores: Map<String, Int> = emptyMap(),
    val tripsCompleted: Int = 0,
    val stampsEarned: Int = 0,
    val weakSpots: List<WeakSpot> = emptyList()
) {
    val bestGrade: Grade? get() = grades.values.minByOrNull { it.ordinal }
}
