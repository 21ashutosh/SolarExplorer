package com.example.solarexplorer.data.model

// NOTE: This model uses correctAnswerIndex (Int) because your QuizScreen expects it.
data class QuizQuestion(
    val planetName: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
