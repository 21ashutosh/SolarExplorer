package com.example.solarexplorer.ui.theme

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun PreviewLight() {
    SolarExplorerTheme(false) {
        Surface {
            Text("Light Theme", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewDark() {
    SolarExplorerTheme(true) {
        Surface {
            Text("Dark Theme", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
