package com.example.solarexplorer.ui

import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.solarexplorer.viewmodel.PlanetViewModel

@OptIn(ExperimentalMaterial3Api::class) // <-- ADD THIS ANNOTATION
@Composable
fun SettingsScreen(vm: PlanetViewModel, onBack: () -> Unit) {
    // Collect flows from ViewModel as Compose state
    val ttsRate by vm.ttsRate.collectAsState()
    val ttsPitch by vm.ttsPitch.collectAsState()
    val autoplay by vm.autoplay.collectAsState()

    // Local editable copies so user can change before saving
    var localRate by remember { mutableStateOf(ttsRate) }
    var localPitch by remember { mutableStateOf(ttsPitch) }
    var localAutoplay by remember { mutableStateOf(autoplay) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") }, navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
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
            Text("TTS Rate: ${"%.2f".format(localRate)}")
            Slider(value = localRate, onValueChange = { localRate = it }, valueRange = 0.5f..2.0f, steps = 6)

            Text("TTS Pitch: ${"%.2f".format(localPitch)}")
            Slider(value = localPitch, onValueChange = { localPitch = it }, valueRange = 0.5f..2.0f, steps = 6)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Autoplay on open")
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = localAutoplay, onCheckedChange = { localAutoplay = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    // Save values to DataStore via ViewModel
                    vm.setTtsRate(localRate)
                    vm.setTtsPitch(localPitch)
                    vm.setAutoplayOnOpen(localAutoplay)
                }) {
                    Text("Save")
                }

                OutlinedButton(onClick = { vm.clearAllData() }) {
                    Text("Reset App Data")
                }
            }
        }
    }
}
