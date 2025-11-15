package com.example.solarexplorer.data.model

import androidx.compose.ui.graphics.Color

data class Planet(
    val name: String,
    val description: String,
    val distanceFromSun: String,
    val gravity: String,
    val imageResId: Int? = null, // optional drawable
    val funFact: String = "",
    val youtubeVideoId: String? = null,
    val backgroundColors: List<Long> = listOf(0xFF000000, 0xFF111111),
    val quizId: String,
    val quiz: List<QuizQuestion> = emptyList(),
    val themeColor: Color = Color.Black
) {
    companion object {
        fun samplePlanet() = Planet(
            name = "Earth",
            description = "Third planet from the Sun, home to millions of species.",
            distanceFromSun = "149.6 million km",
            gravity = "9.8 m/s²",
            imageResId = null,
            funFact = "Earth is the only planet known to support life.",
            youtubeVideoId = null,
            backgroundColors = listOf(0xFF0A0F1A, 0xFF0F1724),
            quizId = "earth_quiz",
            quiz = emptyList(),
            themeColor = Color(0xFF0B3D91)
        )
    }
}
