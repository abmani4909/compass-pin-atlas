package com.meridian.compasspin.data.repository

import com.meridian.compasspin.data.local.AtlasPreferences
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.model.AtlasProgress
import com.meridian.compasspin.domain.model.ClueResult
import com.meridian.compasspin.domain.model.Grade
import com.meridian.compasspin.domain.model.TripResult
import com.meridian.compasspin.domain.model.WeakSpot
import com.meridian.compasspin.domain.repository.AtlasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AtlasRepositoryImpl(private val preferences: AtlasPreferences) : AtlasRepository {

    private val _progress = MutableStateFlow(read())
    override val progress: StateFlow<AtlasProgress> = _progress.asStateFlow()

    override fun recordTrip(result: TripResult) {
        val stored = preferences.gradeOf(result.atlasId)?.let { runCatching { Grade.valueOf(it) }.getOrNull() }
        val improved = stored == null || result.grade.ordinal < stored.ordinal
        val stampable = result.grade.ordinal <= Grade.B.ordinal
        if (improved) {
            preferences.storeAtlas(
                result.atlasId,
                result.grade.name,
                maxOf(result.totalScore, preferences.scoreOf(result.atlasId))
            )
            if (stampable && stored == null) {
                preferences.stampsEarned = preferences.stampsEarned + 1
            }
        }
        preferences.weakSpots = merge(preferences.weakSpots, result.clues)
        preferences.tripsCompleted = preferences.tripsCompleted + 1
        _progress.value = read()
    }

    override fun clearProgress() {
        preferences.clearProgress(SampleData.atlases.map { it.id })
        _progress.value = read()
    }

    private fun read(): AtlasProgress {
        val grades = LinkedHashMap<String, Grade>()
        val scores = LinkedHashMap<String, Int>()
        SampleData.atlases.forEach { atlas ->
            val raw = preferences.gradeOf(atlas.id)
            val grade = raw?.let { runCatching { Grade.valueOf(it) }.getOrNull() }
            if (grade != null) {
                grades[atlas.id] = grade
                scores[atlas.id] = preferences.scoreOf(atlas.id)
            }
        }
        val names = SampleData.places.associate { it.id to it.name }
        val weak = decode(preferences.weakSpots)
            .mapNotNull { entry ->
                val name = names[entry.first] ?: return@mapNotNull null
                WeakSpot(
                    placeId = entry.first,
                    placeName = name,
                    attempts = entry.third,
                    averageKm = entry.second / entry.third
                )
            }
            .sortedByDescending { it.averageKm }
        return AtlasProgress(
            grades = grades,
            bestScores = scores,
            tripsCompleted = preferences.tripsCompleted,
            stampsEarned = preferences.stampsEarned,
            weakSpots = weak
        )
    }

    private fun merge(raw: String, clues: List<ClueResult>): String {
        val totals = LinkedHashMap<String, IntArray>()
        decode(raw).forEach { entry -> totals[entry.first] = intArrayOf(entry.second, entry.third) }
        clues.forEach { clue ->
            val slot = totals.getOrPut(clue.placeId) { intArrayOf(0, 0) }
            slot[0] = slot[0] + clue.distanceKm
            slot[1] = slot[1] + 1
        }
        return totals.entries.joinToString(ENTRY_SEPARATOR) { entry ->
            entry.key + FIELD_SEPARATOR + entry.value[0] + FIELD_SEPARATOR + entry.value[1]
        }
    }

    private fun decode(raw: String): List<Triple<String, Int, Int>> =
        raw.split(ENTRY_SEPARATOR)
            .filter { it.isNotBlank() }
            .mapNotNull { chunk ->
                val parts = chunk.split(FIELD_SEPARATOR)
                if (parts.size != 3) return@mapNotNull null
                val sum = parts[1].toIntOrNull() ?: return@mapNotNull null
                val count = parts[2].toIntOrNull() ?: return@mapNotNull null
                if (count <= 0) null else Triple(parts[0], sum, count)
            }

    private companion object {
        const val ENTRY_SEPARATOR = ";"
        const val FIELD_SEPARATOR = ":"
    }
}
