package com.example.solarexplorer.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.solarexplorer.R
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

    var showResult by remember { mutableStateOf(false) }
    var finalScore by remember { mutableStateOf(0) }
    var finalTotal by remember { mutableStateOf(0) }

    // ---------- RESULT POPUP UI ----------
    if (showResult) {

        val message = when {
            finalScore == finalTotal -> stringResource(R.string.quiz_full_marks)
            finalScore >= finalTotal / 2 -> stringResource(R.string.quiz_good_score)
            else -> stringResource(R.string.quiz_try_again)
        }

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    showResult = false
                    onFinish(finalScore, finalTotal)
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            title = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.quiz_your_score,
                        finalScore,
                        finalTotal
                    ),
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
            TopAppBar(title = {
                Text(stringResource(R.string.quiz_title))
            }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
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
                Text(stringResource(R.string.no_quiz_available))
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
                text = stringResource(
                    R.string.quiz_question_number,
                    index + 1,
                    questions.size
                ),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = q.question,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                q.options.forEachIndexed { i, option ->

                    val interaction = remember { MutableInteractionSource() }   // ← FIXED (each option now separate)

                    val isCorrect = i == q.correctAnswerIndex
                    val isSelected = selectedOption == i

                    // ---- FIXED COLOR LOGIC ----
                    val bg = when {
                        answered && isSelected && isCorrect -> Color(0xFFAAF27A)      // green
                        answered && isSelected && !isCorrect -> Color(0xFFF28B82)    // red
                        answered && isCorrect -> Color(0xFFAAF27A)                   // show correct
                        else -> MaterialTheme.colorScheme.surfaceVariant            // normal
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = bg),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                enabled = !answered,
                                interactionSource = interaction,
                                indication = LocalIndication.current
                            ) {
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
                    Text(stringResource(R.string.reset))
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
                            !answered -> stringResource(R.string.submit)
                            index + 1 < questions.size -> stringResource(R.string.next)
                            else -> stringResource(R.string.finish)
                        }
                    )
                }
            }
        }
    }
}
