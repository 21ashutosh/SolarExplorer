package com.example.solarexplorer.ui

import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.example.solarexplorer.data.model.Planet
import com.example.solarexplorer.viewmodel.QuizViewModel
import com.example.solarexplorer.ui.theme.SolarExplorerTheme
import com.example.solarexplorer.ui.theme.SpaceGradientBackground
import java.util.Locale

// -------------------------
// Planet Detail Screen
// -------------------------
@Composable
fun PlanetDetailScreen(
    planet: Planet?,
    onBack: () -> Unit,
    onQuizClick: (String) -> Unit,
    vm: QuizViewModel
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // ---------------- TEXT TO SPEECH ----------------
    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("en", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Indian English not supported, using US English")
                    tts?.setLanguage(Locale.US)
                }
                tts?.setSpeechRate(1f)
                tts?.setPitch(1f)
            } else {
                Log.e("TTS", "TTS initialization failed")
            }
        }

        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // ---------------- HANDLE NULL PLANET ----------------
    if (planet == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Planet not found", color = Color.White)
        }
        return
    }

    // ---------------- GRADIENT COLORS ----------------
    val colors = planet.backgroundColors.map { Color(it.toULong().toLong()) } // ensure Long -> Color
    val top = colors.getOrNull(0) ?: Color(0xFF0A0F1A)
    val bottom = colors.getOrNull(1) ?: Color(0xFF0F1724)

    SolarExplorerTheme {
        SpaceGradientBackground(topColor = top, bottomColor = bottom) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                // ---------------- BACK BUTTON ----------------
                TextButton(onClick = onBack) {
                    Text("← Back", color = MaterialTheme.colorScheme.secondary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ---------------- PLANET IMAGE ----------------
                planet.imageResId?.let { imageRes ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = planet.name,
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ---------------- PLANET NAME ----------------
                Text(
                    text = planet.name,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ---------------- PLANET DESCRIPTION ----------------
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = planet.description,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ---------------- FACTS ----------------
                Text(
                    text = "Facts",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FactRow(title = "Distance from Sun", value = planet.distanceFromSun)
                        FactRow(title = "Gravity", value = planet.gravity)
                        FactRow(title = "Fun Fact", value = planet.funFact)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ---------------- BUTTONS ----------------
                planet.youtubeVideoId?.let {
                    // Optional YouTube player integration placeholder
                }

                Button(
                    onClick = { tts?.speak(planet.description, TextToSpeech.QUEUE_FLUSH, null, planet.name) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Play Narration", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        tts?.speak("Fun fact about ${planet.name}: ${planet.funFact}", TextToSpeech.QUEUE_FLUSH, null, "${planet.name}_funfact")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Play Fun Fact", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onQuizClick(planet.name) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Take Quiz", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ------------------------- FACT ROW -------------------------
@Composable
fun FactRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.secondary)
    }
}

// ------------------------- PREVIEWS -------------------------
@Preview(showBackground = true)
@Composable
fun PreviewPlanetDetailLight() {
    SolarExplorerTheme(darkTheme = false) {
        PlanetDetailScreen(
            planet = Planet.samplePlanet(),
            onBack = {},
            onQuizClick = {},
            vm = QuizViewModel(context = LocalContext.current)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPlanetDetailDark() {
    SolarExplorerTheme(darkTheme = true) {
        PlanetDetailScreen(
            planet = Planet.samplePlanet(),
            onBack = {},
            onQuizClick = {},
            vm = QuizViewModel(context = LocalContext.current)
        )
    }
}
