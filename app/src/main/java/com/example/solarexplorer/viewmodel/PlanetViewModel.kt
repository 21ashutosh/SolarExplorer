package com.example.solarexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.solarexplorer.data.datastore.DataStoreManager
import com.example.solarexplorer.data.model.Planet
import com.example.solarexplorer.data.model.QuizQuestion
import com.example.solarexplorer.data.repository.PlanetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlanetViewModel(
    private val dataStore: DataStoreManager
) : ViewModel() {

    // -----------------------------
    // PLANET LIST FLOW (from version 1)
    // -----------------------------
    private val _planets = MutableStateFlow(PlanetRepository.getPlanets())
    val planets: StateFlow<List<Planet>> = _planets.asStateFlow()

    // Refresh planets if repository updates
    fun refreshPlanetsFromRepository() {
        _planets.value = PlanetRepository.getPlanets()
    }

    fun getPlanetByName(name: String): Planet? =
        PlanetRepository.getPlanetByName(name)

    // -----------------------------
    // QUIZ DATA (from version 1)
    // -----------------------------
    private val planetQuizzes: Map<String, List<QuizQuestion>> = mapOf(

        "Mercury" to listOf(
            QuizQuestion(
                planetName = "Mercury",
                question = "Which planet is closest to the Sun?",
                options = listOf("Earth", "Mercury", "Venus", "Mars"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Mercury",
                question = "Mercury has no ____?",
                options = listOf("Mountains", "Atmosphere", "Rocks", "Crater"),
                correctAnswerIndex = 1
            )
        ),

        "Venus" to listOf(
            QuizQuestion(
                planetName = "Venus",
                question = "Which planet is known as Earth's twin?",
                options = listOf("Mars", "Jupiter", "Venus", "Neptune"),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                planetName = "Venus",
                question = "Why is Venus the hottest planet?",
                options = listOf("Thick Atmosphere", "Close to Sun", "Covered in Ice"),
                correctAnswerIndex = 0
            )
        ),

        "Earth" to listOf(
            QuizQuestion(
                planetName = "Earth",
                question = "Earth is the ____ planet from the Sun.",
                options = listOf("2nd", "3rd", "4th", "1st"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Earth",
                question = "What percentage of Earth's surface is water?",
                options = listOf("51%", "71%", "20%", "90%"),
                correctAnswerIndex = 1
            )
        ),

        "Mars" to listOf(
            QuizQuestion(
                planetName = "Mars",
                question = "What is Mars known as?",
                options = listOf("Blue Planet", "Red Planet", "Gas Giant"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Mars",
                question = "What are the two moons of Mars?",
                options = listOf("Phobos & Deimos", "Io & Europa", "Titan & Ganymede"),
                correctAnswerIndex = 0
            )
        ),

        "Jupiter" to listOf(
            QuizQuestion(
                planetName = "Jupiter",
                question = "Jupiter is the ____ planet in the solar system.",
                options = listOf("Smallest", "Largest", "Coldest"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Jupiter",
                question = "What is the giant storm on Jupiter called?",
                options = listOf("Black Spot", "White Oval", "Great Red Spot"),
                correctAnswerIndex = 2
            )
        ),

        "Saturn" to listOf(
            QuizQuestion(
                planetName = "Saturn",
                question = "What is Saturn famous for?",
                options = listOf("Its mountains", "Its rings", "Its oceans"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Saturn",
                question = "What is Saturn mainly made of?",
                options = listOf("Rock", "Gas", "Ice"),
                correctAnswerIndex = 1
            )
        ),

        "Uranus" to listOf(
            QuizQuestion(
                planetName = "Uranus",
                question = "Uranus rotates on its ____.",
                options = listOf("Side", "Top", "Bottom"),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                planetName = "Uranus",
                question = "What is the main color of Uranus?",
                options = listOf("Blue-green", "Red", "Yellow"),
                correctAnswerIndex = 0
            )
        ),

        "Neptune" to listOf(
            QuizQuestion(
                planetName = "Neptune",
                question = "Neptune is known for its ____ winds.",
                options = listOf("Slow", "Fastest", "Hot"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Neptune",
                question = "What is Neptune's main color?",
                options = listOf("Red", "Deep Blue", "Brown"),
                correctAnswerIndex = 1
            )
        )
    )

    fun getQuizForPlanet(planet: Planet?): List<QuizQuestion> =
        planetQuizzes[planet?.name] ?: emptyList()

    fun getQuizForPlanetName(name: String): List<QuizQuestion> =
        planetQuizzes[name] ?: emptyList()

    // -----------------------------
    // DATASTORE - SETTINGS + QUIZ SCORES
    // -----------------------------

    val ttsRate: StateFlow<Float> = dataStore.ttsRateFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, DataStoreManager.DEFAULT_TTS_RATE)

    val ttsPitch: StateFlow<Float> = dataStore.ttsPitchFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, DataStoreManager.DEFAULT_TTS_PITCH)

    val autoplay: StateFlow<Boolean> = dataStore.autoplayFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, DataStoreManager.DEFAULT_AUTOPLAY)

    fun quizHighScoreFlow(planetName: String) =
        dataStore.quizHighScoreFlow(planetName)

    fun quizAttemptsFlow(planetName: String) =
        dataStore.quizAttemptsFlow(planetName)

    fun setTtsRate(rate: Float) {
        viewModelScope.launch { dataStore.setTtsRate(rate) }
    }

    fun setTtsPitch(pitch: Float) {
        viewModelScope.launch { dataStore.setTtsPitch(pitch) }
    }

    fun setAutoplayOnOpen(enabled: Boolean) {
        viewModelScope.launch { dataStore.setAutoplayOnOpen(enabled) }
    }

    fun saveQuizResult(planetName: String, score: Int) {
        viewModelScope.launch { dataStore.saveQuizResult(planetName, score) }
    }

    fun clearAllData() {
        viewModelScope.launch { dataStore.clearAll() }
    }
}
