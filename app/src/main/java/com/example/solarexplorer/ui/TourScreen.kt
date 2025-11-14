// app/src/main/java/com/example/solarexplorer/ui/TourScreen.kt
package com.example.solarexplorer.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.solarexplorer.data.model.Planet
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.max
// ... other imports
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api // <-- 1. Import the annotation
import androidx.compose.runtime.*
// ... other imports

@OptIn(ExperimentalMaterial3Api::class) // <-- 2. Add the annotation here
@Composable
fun TourScreen(planets: List<Planet>, onBack: () -> Unit) {
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context) { /* init */ } }
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

    // Helper estimate: approx 300ms per word (coarse)
    fun estimateMsForText(text: String): Long {
        val words = text.split("\\s+".toRegex()).size
        return max(1200L, words * 300L) // minimum 1.2s otherwise word-based
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
                // wait approximate length + 400ms buffer
                delay(wait + 400)
                currentIndex += 1
            }
            // finished
            isTourPlaying = false
            currentIndex = 0
        } else {
            tts.stop()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Solar Tour") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Play a guided tour of the planets. Each planet's name and short description will be read aloud.")

            if (planets.isEmpty()) {
                Text("No planets found.")
                return@Scaffold
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Now showing:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = if (isTourPlaying) planets[currentIndex.coerceAtMost(planets.size - 1)].name else "Tour stopped")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { isTourPlaying = true }, enabled = !isTourPlaying) {
                    Text("Start Tour")
                }
                OutlinedButton(onClick = { isTourPlaying = false }, enabled = isTourPlaying) {
                    Text("Stop")
                }
            }
        }
    }
}
