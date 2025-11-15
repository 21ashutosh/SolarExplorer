// app/src/main/java/com/example/solarexplorer/ui/TourScreen.kt
package com.example.solarexplorer.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.solarexplorer.data.model.Planet
import com.example.solarexplorer.R
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourScreen(planets: List<Planet>, onBack: () -> Unit) {

    val context = LocalContext.current
    val tts = remember { TextToSpeech(context) { } }

    DisposableEffect(tts) {
        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    var isTourPlaying by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }

    fun estimateMsForText(text: String): Long {
        val words = text.split("\\s+".toRegex()).size
        return max(1200L, words * 300L) // minimum 1.2s
    }

    // Play / stop logic
    LaunchedEffect(isTourPlaying) {
        if (isTourPlaying) {
            currentIndex = 0
            while (currentIndex < planets.size && isTourPlaying) {
                val p = planets[currentIndex]
                val textToSpeak = "${p.name}. ${p.description}"
                tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, p.name)
                val wait = estimateMsForText(textToSpeak)
                delay(wait + 400)
                currentIndex += 1
            }

            isTourPlaying = false
            currentIndex = 0

        } else {
            tts.stop()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.solar_tour_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = stringResource(id = R.string.tour_description)
            )

            if (planets.isEmpty()) {
                Text(text = stringResource(id = R.string.no_planets_found))
                return@Scaffold
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text(stringResource(id = R.string.now_showing))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text =
                            if (isTourPlaying)
                                planets[currentIndex.coerceAtMost(planets.size - 1)].name
                            else
                                stringResource(id = R.string.tour_stopped)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = { isTourPlaying = true },
                    enabled = !isTourPlaying
                ) {
                    Text(stringResource(id = R.string.start_tour))
                }

                OutlinedButton(
                    onClick = { isTourPlaying = false },
                    enabled = isTourPlaying
                ) {
                    Text(stringResource(id = R.string.stop_tour))
                }
            }
        }
    }
}
