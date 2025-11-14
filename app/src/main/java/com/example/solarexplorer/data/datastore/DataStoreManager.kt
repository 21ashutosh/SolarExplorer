package com.example.solarexplorer.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to create DataStore instance
private const val DATASTORE_NAME = "solarexplorer_prefs"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreManager(private val context: Context) {

    // Keys
    private object Keys {
        val TTS_RATE = floatPreferencesKey("tts_rate")            // default 1.0f
        val TTS_PITCH = floatPreferencesKey("tts_pitch")          // default 1.0f
        val AUTOPLAY_ON_OPEN = booleanPreferencesKey("autoplay_on_open")
        // quiz highscore keys are dynamic per planet; see helper below
    }

    // DEFAULTS
    companion object {
        const val DEFAULT_TTS_RATE = 1.0f
        const val DEFAULT_TTS_PITCH = 1.0f
        const val DEFAULT_AUTOPLAY = false
    }

    private fun safePlanetKey(planetName: String): String {
        // make a stable key for planet names: lower-case, underscores, remove non-alphanum
        return "quiz_highscore_" + planetName.trim()
            .lowercase()
            .replace("\\s+".toRegex(), "_")
            .replace("[^a-z0-9_]".toRegex(), "")
    }

    private fun highScoreKeyFor(planetName: String) = intPreferencesKey(safePlanetKey(planetName))
    private fun attemptsKeyFor(planetName: String) = intPreferencesKey("${safePlanetKey(planetName)}_attempts")

    // --- Read flows ---

    val ttsRateFlow: Flow<Float> = context.dataStore.data
        .map { prefs -> prefs[Keys.TTS_RATE] ?: DEFAULT_TTS_RATE }

    val ttsPitchFlow: Flow<Float> = context.dataStore.data
        .map { prefs -> prefs[Keys.TTS_PITCH] ?: DEFAULT_TTS_PITCH }

    val autoplayFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[Keys.AUTOPLAY_ON_OPEN] ?: DEFAULT_AUTOPLAY }

    fun quizHighScoreFlow(planetName: String): Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[highScoreKeyFor(planetName)] ?: 0 }

    fun quizAttemptsFlow(planetName: String): Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[attemptsKeyFor(planetName)] ?: 0 }

    // --- Write operations (suspend) ---

    suspend fun setTtsRate(rate: Float) {
        context.dataStore.edit { prefs -> prefs[Keys.TTS_RATE] = rate }
    }

    suspend fun setTtsPitch(pitch: Float) {
        context.dataStore.edit { prefs -> prefs[Keys.TTS_PITCH] = pitch }
    }

    suspend fun setAutoplayOnOpen(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.AUTOPLAY_ON_OPEN] = enabled }
    }

    // Save quiz result: update highscore if newScore > existing, and increment attempts
    suspend fun saveQuizResult(planetName: String, score: Int) {
        val key = highScoreKeyFor(planetName)
        val attemptsKey = attemptsKeyFor(planetName)
        context.dataStore.edit { prefs ->
            val currentHigh = prefs[key] ?: 0
            if (score > currentHigh) {
                prefs[key] = score
            }
            val attempts = prefs[attemptsKey] ?: 0
            prefs[attemptsKey] = attempts + 1
        }
    }

    // Reset all preferences (use with caution)
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
