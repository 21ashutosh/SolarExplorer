package com.example.solarexplorer.ui

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.solarexplorer.data.model.Planet
import com.example.solarexplorer.ui.components.YouTubePlayer
import java.util.Locale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.solarexplorer.viewmodel.QuizViewModel

// -------------------------
// Reusable Beautiful Planet Button
// -------------------------
@Composable
fun PlanetButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring()
    )

    Button(
        onClick = {
            pressed = true
            onClick()
            pressed = false
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.85f),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, fontSize = 18.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetDetailScreen(
    planet: Planet?,
    onBack: () -> Unit,
    onQuizClick: (String) -> Unit,
    vm: QuizViewModel   // ← you must have this ViewModel in your project
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // --- Text To Speech Initialization ---
    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val localeResult = tts?.setLanguage(Locale("en", "IN"))
                if (localeResult == TextToSpeech.LANG_MISSING_DATA ||
                    localeResult == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "Indian English not supported. Using US English.")
                    tts?.setLanguage(Locale.US)
                } else {
                    Log.i("TTS", "Indian English set successfully.")
                }
                tts?.setSpeechRate(1.0f)
                tts?.setPitch(1.0f)
            } else {
                Log.e("TTS", "TTS initialization failed.")
            }
        }

        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(planet?.name ?: "Planet") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        if (planet == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Planet not found")
            }
            return@Scaffold
        }

        // Background Gradient
        val colors = planet.backgroundColors.map { Color(it) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colors))
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // --- YouTube Player Section ---
                if (planet.youtubeVideoId != null) {
                    Text(
                        text = "Watch and Learn:",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    YouTubePlayer(
                        videoId = planet.youtubeVideoId,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                }

                // --- Planet Details ---
                Text(text = planet.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Distance: ${planet.distanceFromSun}")
                Text(text = "Gravity: ${planet.gravity}")

                Divider()

                Text(text = planet.description)

                if (planet.funFact.isNotBlank()) {
                    Text(
                        text = "Fun fact: ${planet.funFact}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // -------------------------
                // REPLACED OLD BUTTONS WITH NEW STYLISH BUTTONS
                // -------------------------

                PlanetButton(
                    text = "Play Narration",
                    icon = Icons.Default.PlayArrow,
                    color = Color(planet.backgroundColors.first()),
                    onClick = {
                        tts?.speak(
                            planet.description,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            planet.name
                        )
                    }
                )

                PlanetButton(
                    text = "Play Fun Fact",
                    icon = Icons.Default.Lightbulb,
                    color = Color(planet.backgroundColors.first()),
                    onClick = {
                        tts?.speak(
                            "Fun fact about ${planet.name}. ${planet.funFact}",
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            "${planet.name}_fact"
                        )
                    }
                )

                PlanetButton(
                    text = "Take Quiz",
                    icon = Icons.Default.Quiz,
                    color = Color(planet.backgroundColors.first()),
                    onClick = { onQuizClick(planet.name) }
                )

                // -------------------------
                // INSERTED YOUR CODE HERE (MERGED)
                // -------------------------
                val highScoreFlow = vm.quizHighScoreFlow(planet.name)
                val highScore by highScoreFlow.collectAsState(initial = 0)

                Text(
                    text = "High score: $highScore",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
