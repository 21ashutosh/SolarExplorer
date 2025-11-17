package com.example.solarexplorer.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Extension property to create DataStore for Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "quiz_scores")

class QuizViewModel(private val context: Context) : ViewModel() {

    // Generate a key for each planet's high score
    private fun highScoreKey(planetName: String): Preferences.Key<Int> {
        return intPreferencesKey("${planetName}_highscore")
    }

    // Read high score as Flow (UI updates automatically)
    fun quizHighScoreFlow(planetName: String): Flow<Int> {
        return context.dataStore.data
            .map { preferences ->
                preferences[highScoreKey(planetName)] ?: 0
            }
    }

    // Save high score
    fun updateHighScore(planetName: String, newScore: Int) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                val currentScore = preferences[highScoreKey(planetName)] ?: 0
                if (newScore > currentScore) {
                    preferences[highScoreKey(planetName)] = newScore
                }
            }
        }
    }
}
