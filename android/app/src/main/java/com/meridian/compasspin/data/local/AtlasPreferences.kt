package com.meridian.compasspin.data.local

import android.content.Context
import android.content.SharedPreferences

class AtlasPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun gradeOf(regionId: String): String? = prefs.getString(KEY_GRADE + regionId, null)

    fun scoreOf(regionId: String): Int = prefs.getInt(KEY_SCORE + regionId, 0)

    fun storeAtlas(regionId: String, grade: String, score: Int) {
        prefs.edit().putString(KEY_GRADE + regionId, grade).putInt(KEY_SCORE + regionId, score).apply()
    }

    var tripsCompleted: Int
        get() = prefs.getInt(KEY_TRIPS, 0)
        set(value) = prefs.edit().putInt(KEY_TRIPS, value).apply()

    var stampsEarned: Int
        get() = prefs.getInt(KEY_STAMPS, 0)
        set(value) = prefs.edit().putInt(KEY_STAMPS, value).apply()

    var weakSpots: String
        get() = prefs.getString(KEY_WEAK, "") ?: ""
        set(value) = prefs.edit().putString(KEY_WEAK, value).apply()

    var useMiles: Boolean
        get() = prefs.getBoolean(KEY_MILES, false)
        set(value) = prefs.edit().putBoolean(KEY_MILES, value).apply()

    var hardBorders: Boolean
        get() = prefs.getBoolean(KEY_HARD, false)
        set(value) = prefs.edit().putBoolean(KEY_HARD, value).apply()

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    fun clearProgress(regionIds: List<String>) {
        val editor = prefs.edit()
        regionIds.forEach { editor.remove(KEY_GRADE + it).remove(KEY_SCORE + it) }
        editor.remove(KEY_WEAK).putInt(KEY_TRIPS, 0).putInt(KEY_STAMPS, 0).apply()
    }

    private companion object {
        const val STORE = "compass_pin_atlas"
        const val KEY_GRADE = "bestGrade_"
        const val KEY_SCORE = "bestScore_"
        const val KEY_TRIPS = "tripsCompleted"
        const val KEY_STAMPS = "stampsEarned"
        const val KEY_WEAK = "weakSpots"
        const val KEY_MILES = "unitMiles"
        const val KEY_HARD = "hardBorders"
        const val KEY_SOUND = "soundEnabled"
    }
}
