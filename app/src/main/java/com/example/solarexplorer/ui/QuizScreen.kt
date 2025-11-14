package com.example.solarexplorer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.solarexplorer.data.model.QuizQuestion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    questions: List<QuizQuestion>,
    onFinish: (score: Int, maxScore: Int) -> Unit,
    onBack: () -> Unit
) {

    var index by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var answered by remember { mutableStateOf(false) }

    // RESULT POPUP STATES
    var showResult by remember { mutableStateOf(false) }
    var finalScore by remember { mutableStateOf(0) }
    var finalTotal by remember { mutableStateOf(0) }

    // ---------- RESULT POPUP UI ----------
    if (showResult) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    showResult = false
                    onFinish(finalScore, finalTotal)
                }) {
                    Text("OK")
                }
            },
            title = {
                val message =
                    when {
                        finalScore == finalTotal -> "🎉 Congratulations!!!"
                        finalScore >= finalTotal / 2 -> "😄 Nice! You scored good marks!"
                        else -> "🙂 Good Try! You can do even better!"
                    }

                Text(
                    text = message,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Your Score: $finalScore / $finalTotal",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    // ---------- QUIZ UI ----------
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Quiz") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { padding ->
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No quiz available for this planet.")
            }
            return@Scaffold
        }

        val q = questions[index]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Question ${index + 1} of ${questions.size}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = q.question,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                q.options.forEachIndexed { i, option ->

                    val isCorrect = i == q.correctAnswerIndex
                    val isSelected = selectedOption == i

                    val bg = when {
                        isSelected && isCorrect -> Color(0xFFAAF27A)       // green
                        answered && isSelected && !isCorrect -> Color(0xFFF28B82) // red
                        answered && isCorrect -> Color(0xFFAAF27A)        // show correct
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = bg),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !answered) {
                                selectedOption = i
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    if (!answered) selectedOption = i
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(option)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                OutlinedButton(onClick = {
                    selectedOption = null
                }) {
                    Text("Reset")
                }

                Button(onClick = {
                    if (!answered) {
                        val chosen = selectedOption
                        if (chosen != null && chosen == q.correctAnswerIndex) {
                            score++
                        }
                        answered = true
                    } else {
                        if (index + 1 < questions.size) {
                            index++
                            selectedOption = null
                            answered = false
                        } else {
                            finalScore = score
                            finalTotal = questions.size
                            showResult = true
                        }
                    }
                }) {

                    Text(
                        when {
                            !answered -> "Submit"
                            index + 1 < questions.size -> "Next"
                            else -> "Finish"
                        }
                    )
                }
            }
        }
    }
}
