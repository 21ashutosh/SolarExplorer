package com.example.solarexplorer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Create DataStore
private val Context.dataStore by preferencesDataStore(name = "quiz_scores")

class QuizViewModel(private val context: Context) : ViewModel() {

    // Generate a key for each planet’s high score
    private fun highScoreKey(planetName: String): Preferences.Key<Int> {
        return intPreferencesKey("${planetName}_highscore")
    }

    // Read high score as Flow (UI updates automatically)
    fun quizHighScoreFlow(planetName: String): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[highScoreKey(planetName)] ?: 0
        }
    }

    // Save high score
    fun updateHighScore(planetName: String, newScore: Int) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                val current = prefs[highScoreKey(planetName)] ?: 0
                if (newScore > current) {
                    prefs[highScoreKey(planetName)] = newScore
                }
            }
        }
    }
}
