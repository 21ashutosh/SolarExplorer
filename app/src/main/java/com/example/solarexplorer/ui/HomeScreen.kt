@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.solarexplorer.ui

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.solarexplorer.utils.TTSHelper
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.launch
import java.util.Locale

import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Job
import androidx.compose.runtime.mutableStateOf // ensure this import is present
import androidx.compose.runtime.getValue // ensure this import is present
import androidx.compose.runtime.setValue // ensure this import is present

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.solarexplorer.data.model.Planet
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Uncomment if you want Coil's AsyncImage
// import coil.compose.AsyncImage
import com.example.solarexplorer.R
import androidx.compose.material3.CardDefaults // Add this import at the top



import androidx.compose.material3.ExperimentalMaterial3Api // <-- Add this import

@OptIn(ExperimentalMaterial3Api::class) // <-- Add this annotation


@Composable
fun HomeScreen(planets: List<Planet>, onPlanetClick: (String) -> Unit) {
    val context = LocalContext.current

    // TTS helper - remember so it survives recompositions
    val ttsHelper = remember { TTSHelper(context) }

    // Ensure we shutdown TTS when HomeScreen leaves composition
    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    // coroutine scope and job so we can cancel the tour
    val coroutineScope = rememberCoroutineScope()
    var tourJob by remember { mutableStateOf<Job?>(null) }
    var isTouring by remember { mutableStateOf(false) }
    Scaffold(topBar = { TopAppBar(title = { Text("Solar Explorer") }) }) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            // ---- Lottie banner with click to toggle Tour ----
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.solar_system))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        // Toggle start/stop tour
                        if (isTouring) {
                            // stop: cancel job and stop tts immediately
                            tourJob?.cancel()
                            ttsHelper.stop()
                            isTouring = false
                            tourJob = null
                        } else {
                            // start tour: launch coroutine that speaks each planet
                            isTouring = true
                            tourJob = coroutineScope.launch {
                                try {
                                    // Build short texts to speak for each planet
                                    val texts = planets.map { p -> " ${p.name}. ${p.funFact}" }
                                    // Speak sequentially; this suspends until each utterance finishes
                                    ttsHelper.speakSequentially(texts, delayBetweenMs = 400L)
                                } catch (e: Exception) {
                                    // handle cancellation or errors (canceled -> just stop)
                                } finally {
                                    isTouring = false
                                    tourJob = null
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // show the Lottie banner animation
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )

                // Optional: overlay a small play/stop icon so user knows state
                IconButton(
                    onClick = {
                        // same toggle logic as the Box clickable (keeps behavior same)
                        if (isTouring) {
                            tourJob?.cancel()
                            ttsHelper.stop()
                            isTouring = false
                            tourJob = null
                        } else {
                            isTouring = true
                            tourJob = coroutineScope.launch {
                                try {
                                    val texts = planets.map { p -> " ${p.name}. ${p.funFact}" }
                                    ttsHelper.speakSequentially(texts, delayBetweenMs = 700L)
                                } finally {
                                    isTouring = false
                                    tourJob = null
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    if (isTouring) Icon(Icons.Default.Stop, contentDescription = "Stop tour")
                    else Icon(Icons.Default.PlayArrow, contentDescription = "Start tour")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ---- Planet list ----
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(planets) { planet ->
                    PlanetCard(planet = planet, onClick = { onPlanetClick(planet.name) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // <-- Add this annotation
@Composable
fun PlanetCard(planet: Planet, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {

            // gentle rotation animation for each planet thumbnail
            val infiniteTransition = rememberInfiniteTransition()
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 20000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            val imageModifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                //.graphicsLayer { rotationZ = rotation }
                .graphicsLayer {
                    rotationZ = rotation
                    translationX = 10f * kotlin.math.cos(Math.toRadians(rotation.toDouble())).toFloat()
                    translationY = 10f * kotlin.math.sin(Math.toRadians(rotation.toDouble())).toFloat()
                }

            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.orbit_ring))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )



            // If you have local drawable resources:
            val painter = if (planet.imageResId != null) {
                painterResource(id = planet.imageResId)
            } else {
                painterResource(id = R.drawable.ic_planet_placeholder)
            }
            Box(contentAlignment = Alignment.Center) {
                // Orbit ring animation
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.size(100.dp)
                )
                // Planet image inside the orbit
                Image(
                    painter = painter,
                    contentDescription = planet.name,
                    modifier = Modifier.size(64.dp).clip(CircleShape)
                )
            }
//            Image(
//                painter = painter,
//                contentDescription = planet.name,
//                modifier = imageModifier
//            )
//            Box(
//                modifier = imageModifier
//                    .shadow(elevation = 8.dp, shape = CircleShape, clip = true)
//            ) {
//                Image(
//                    painter = painter,
//                    contentDescription = planet.name,
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .size(70.dp)
//                    .clip(CircleShape)
//                    .background(Color(0x66FFD700)) // semi-transparent yellow glow
//                    .align(Alignment.CenterVertically)
//            )




            // If you prefer remote images, replace the Image above with Coil's AsyncImage:
            /*
            AsyncImage(
                model = planet.imageUrl, // add this field to Planet if using remote images
                contentDescription = planet.name,
                modifier = imageModifier
            )
            */

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = planet.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = planet.distanceFromSun, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
