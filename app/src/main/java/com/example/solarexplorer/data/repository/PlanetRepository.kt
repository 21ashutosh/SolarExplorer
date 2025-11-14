package com.example.solarexplorer.data.repository

import androidx.compose.ui.graphics.Color
import com.example.solarexplorer.R
import com.example.solarexplorer.data.model.Planet
import com.example.solarexplorer.data.model.QuizQuestion

object PlanetRepository {

    private val planets = listOf(
        Planet(
            name = "Mercury",
            description = "Mercury is the smallest planet and closest to the Sun.",
            distanceFromSun = "57.9 million km",
            gravity = "3.7 m/s²",
            imageResId = R.drawable.mercury,
            funFact = "A year on Mercury is just 88 Earth days.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFFB0BEC5, 0xFF546E7A),  // Silver → Space Blue

                    quizId = "mercury_quiz",
            themeColor = Color(0xFFB0BEC5)
        ),
        Planet(
            name = "Venus",
            description = "Venus has a thick toxic atmosphere and is very hot.",
            distanceFromSun = "108.2 million km",
            gravity = "8.87 m/s²",
            imageResId = R.drawable.venus,
            funFact = "Venus rotates backwards compared to most planets.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFFFFC796, 0xFFFF6B6B),  // Soft Orange → Warm Red

                    quizId = "venus_quiz",
            themeColor = Color(0xFFFFC107),
        ),
        Planet(
            name = "Earth",
            description = "Earth is our home — the only known planet with life.",
            distanceFromSun = "149.6 million km",
            gravity = "9.81 m/s²",
            imageResId = R.drawable.earth,
            funFact = "About 71% of Earth's surface is water.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFF6DD5FA, 0xFF2980B9)  // Sky Blue → Deep Blue
            ,
            quizId = "earth_quiz",
            themeColor = Color(0xFF42A5F5)
        ),
        Planet(
            name = "Mars",
            description = "Mars is called the Red Planet because of iron oxide on its surface.",
            distanceFromSun = "227.9 million km",
            gravity = "3.71 m/s²",
            imageResId = R.drawable.mars,
            funFact = "Mars hosts the tallest volcano in the solar system, Olympus Mons.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFFFF6E40, 0xFFD84315)  // Bright Rust → Deep Orange
            ,
            quizId = "mars_quiz",
            themeColor = Color(0xFFE53935),
        ),
        Planet(
            name = "Jupiter",
            description = "Jupiter is the largest planet and a gas giant.",
            distanceFromSun = "778.5 million km",
            gravity = "24.79 m/s²",
            imageResId = R.drawable.jupiter,
            funFact = "Jupiter's Great Red Spot is a giant storm larger than Earth.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFFF3E5AB, 0xFFCC7722)  // Cream → Amber Brown
            ,
            quizId = "jupiter_quiz",
            themeColor = Color(0xFFD78848) // Orange-Brown

        ),
        Planet(
            name = "Saturn",
            description = "Saturn is famous for its bright ring system.",
            distanceFromSun = "1.43 billion km",
            gravity = "10.44 m/s²",
            imageResId = R.drawable.saturn,
            funFact = "Saturn has dozens of moons — Titan is larger than Mercury.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFFF6EEC7, 0xFFD2B48C)  // Beige → Sandy Brown
            ,
            quizId = "saturn_quiz",
            themeColor = Color(0xFFF2C879) // Soft Gold

        ),
        Planet(
            name = "Uranus",
            description = "Uranus is an ice giant that rotates on its side.",
            distanceFromSun = "2.87 billion km",
            gravity = "8.69 m/s²",
            imageResId = R.drawable.uranus,
            funFact = "Uranus appears blue-green because of methane in its atmosphere.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFFA0E9FF, 0xFF00B4D8)  // Aqua → Cerulean Blue
            ,
            quizId = "uranus_quiz",
            themeColor = Color(0xFF7FDBFF) // Icy Cyan

        ),
        Planet(
            name = "Neptune",
            description = "Neptune is a cold, blue ice giant and the farthest known planet.",
            distanceFromSun = "4.50 billion km",
            gravity = "11.15 m/s²",
            imageResId = R.drawable.neptune,
            funFact = "Neptune has extremely strong winds — the fastest in the solar system.",
            youtubeVideoId = "dQw4w9WgXcQ",
            backgroundColors = listOf(0xFF6B8DD6, 0xFF1E3A8A)  // Soft Blue → Deep Royal Blue
            ,
            quizId = "neptune_quiz",themeColor = Color(0xFF3A4CC0) // Deep Space Blue

        )
    )

    fun getPlanets(): List<Planet> = planets
    fun getPlanetByName(name: String): Planet? = planets.find { it.name == name }

    // ----------------------------------------------------------------------
    // QUIZZES (with planetName added)
    // ----------------------------------------------------------------------

    private val quizQuestions: Map<String, List<QuizQuestion>> = mapOf(

        // MERCURY
        "mercury_quiz" to listOf(
            QuizQuestion(
                planetName = "Mercury",
                question = "How long is a year on Mercury?",
                options = listOf("88 Earth days", "365 days", "687 Earth days", "30 days"),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                planetName = "Mercury",
                question = "Which is true about Mercury?",
                options = listOf("Has thick clouds", "Smallest planet", "Has rings", "Is blue"),
                correctAnswerIndex = 1
            )
        ),

        // VENUS
        "venus_quiz" to listOf(
            QuizQuestion(
                planetName = "Venus",
                question = "Why is Venus so hot?",
                options = listOf("It has no atmosphere", "Thick CO₂ traps heat", "It is on fire", "It is close to Earth"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Venus",
                question = "What is unique about Venus’ rotation?",
                options = listOf("It rotates sideways", "It rotates backwards", "It doesn't rotate", "It rotates very fast"),
                correctAnswerIndex = 1
            )
        ),

        // EARTH
        "earth_quiz" to listOf(
            QuizQuestion(
                planetName = "Earth",
                question = "What percent of Earth is water?",
                options = listOf("10%", "71%", "40%", "85%"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Earth",
                question = "What is Earth's gravity?",
                options = listOf("3.7 m/s²", "9.81 m/s²", "24.79 m/s²", "11.15 m/s²"),
                correctAnswerIndex = 1
            )
        ),

        // MARS
        "mars_quiz" to listOf(
            QuizQuestion(
                planetName = "Mars",
                question = "What is Mars also called?",
                options = listOf("Blue planet", "Red planet", "Gas giant", "Ice giant"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Mars",
                question = "Mars has the tallest volcano named:",
                options = listOf("Vesuvius", "Olympus Mons", "Mount Everest", "Etna"),
                correctAnswerIndex = 1
            )
        ),

        // JUPITER
        "jupiter_quiz" to listOf(
            QuizQuestion(
                planetName = "Jupiter",
                question = "What is Jupiter known for?",
                options = listOf("Big rings", "Strong winds", "Great Red Spot", "Tilted rotation"),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                planetName = "Jupiter",
                question = "Jupiter is a:",
                options = listOf("Gas giant", "Ice giant", "Dwarf planet", "Rocky planet"),
                correctAnswerIndex = 0
            )
        ),

        // SATURN
        "saturn_quiz" to listOf(
            QuizQuestion(
                planetName = "Saturn",
                question = "What is Saturn famous for?",
                options = listOf("Its moons", "Its rings", "Its volcanoes", "Its blue color"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Saturn",
                question = "Which moon of Saturn is larger than Mercury?",
                options = listOf("Europa", "Ganymede", "Titan", "Enceladus"),
                correctAnswerIndex = 2
            )
        ),

        // URANUS
        "uranus_quiz" to listOf(
            QuizQuestion(
                planetName = "Uranus",
                question = "Why does Uranus look blue-green?",
                options = listOf("Water oceans", "Methane gas", "Ice crystals", "Storms"),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                planetName = "Uranus",
                question = "Uranus rotates:",
                options = listOf("Very fast", "Backwards", "On its side", "Not at all"),
                correctAnswerIndex = 2
            )
        ),

        // NEPTUNE
        "neptune_quiz" to listOf(
            QuizQuestion(
                planetName = "Neptune",
                question = "Neptune is known for:",
                options = listOf("Fastest winds", "Brightest rings", "Red surface", "Green color"),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                planetName = "Neptune",
                question = "Neptune is the:",
                options = listOf("Closest planet", "Smallest planet", "Coldest planet", "Farthest known planet"),
                correctAnswerIndex = 3
            )
        )
    )

    fun getQuizForId(quizId: String?): List<QuizQuestion> {
        if (quizId == null) return emptyList()
        return quizQuestions[quizId] ?: emptyList()
    }
}
