package com.example.solarexplorer.data.model

import androidx.compose.ui.graphics.Color

data class Planet(
    val name: String,
    val description: String,
    val distanceFromSun: String,
    val gravity: String,
    val imageResId: Int? = null,   // optional drawable resource id
    val funFact: String = "",
    val youtubeVideoId: String? = null,
    val backgroundColors: List<Long> = listOf(0xFF000000, 0xFF111111),
    val quizId: String,
    val quiz: List<QuizQuestion> = emptyList(),
    val themeColor: Color,
)
