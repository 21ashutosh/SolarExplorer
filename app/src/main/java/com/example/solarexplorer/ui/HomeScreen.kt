@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.solarexplorer.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.solarexplorer.utils.TTSHelper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop

import com.airbnb.lottie.compose.*
import kotlinx.coroutines.launch

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Job
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import com.example.solarexplorer.data.model.Planet
import com.example.solarexplorer.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults

// ⭐ Your theme & AppBar imports
import com.example.solarexplorer.ui.theme.SolarExplorerTheme
import com.example.solarexplorer.ui.components.ThemedAppBar

// ⭐ Firebase imports
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(planets: List<Planet>,
               onPlanetClick: ((String) -> Unit)) {

    val context = LocalContext.current
    val ttsHelper = remember { TTSHelper(context) }

    DisposableEffect(Unit) {
        onDispose { ttsHelper.shutdown() }
    }

    val coroutineScope = rememberCoroutineScope()
    var tourJob by remember { mutableStateOf<Job?>(null) }
    var isTouring by remember { mutableStateOf(false) }

    // ⭐ Username State
    var userName by remember { mutableStateOf("Explorer") }

    // ⭐ Fetch from Firebase Firestore
    DisposableEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")    // Your collection
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val firstName = doc.getString("firstName")
                    if (!firstName.isNullOrEmpty()) {
                        userName = firstName
                    }
                }
        }
        onDispose {}
    }

    // ⭐ Wrap whole screen with your theme
    SolarExplorerTheme {

        Scaffold(
            topBar = {
                ThemedAppBar(
                    titleText = stringResource(id = R.string.app_name),
                    onBack = {},
                    rightContent = {
                        // ⭐ Animated Hello Message
                        val infiniteTransition = rememberInfiniteTransition()
                        val alphaAnim by infiniteTransition.animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        Text(
                            text = "Hello, $userName 👋",
                            modifier = Modifier.padding(end = 12.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = alphaAnim),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                // ------------ Lottie Banner ------------
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.solar_system)
                )
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clickable {
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
                                        ttsHelper.speakSequentially(texts, delayBetweenMs = 400L)
                                    } finally {
                                        isTouring = false
                                        tourJob = null
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    IconButton(
                        onClick = {
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
                        if (isTouring)
                            Icon(
                                Icons.Default.Stop,
                                contentDescription = stringResource(id = R.string.stop_tour)
                            )
                        else
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = stringResource(id = R.string.start_tour)
                            )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ------------ Planet List ------------
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(planets) { planet ->
                        PlanetCard(
                            planet = planet,
                            onClick = { onPlanetClick(planet.name) }
                        )
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetCard(planet: Planet, onClick: () -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val infiniteTransition = rememberInfiniteTransition()
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(20000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            val painter = planet.imageResId?.let { painterResource(id = it) }
                ?: painterResource(id = R.drawable.ic_planet_placeholder)

            val orbitComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.orbit_ring)
            )
            val orbitProgress by animateLottieCompositionAsState(
                composition = orbitComposition,
                iterations = LottieConstants.IterateForever
            )

            Box(contentAlignment = Alignment.Center) {

                LottieAnimation(
                    composition = orbitComposition,
                    progress = orbitProgress,
                    modifier = Modifier.size(100.dp)
                )

                Image(
                    painter = painter,
                    contentDescription = planet.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = planet.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = planet.distanceFromSun, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
