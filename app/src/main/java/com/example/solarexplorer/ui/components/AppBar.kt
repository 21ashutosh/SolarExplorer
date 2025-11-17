package com.example.solarexplorer.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.solarexplorer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedAppBar(
    titleText: String,
    onBack: () -> Unit,
    rightContent: (@Composable () -> Unit)? = null // ⭐ Optional slot for right content
) {
    TopAppBar(
        title = {
            Text(
                text = titleText, // use the passed titleText instead of always stringResource(R.string.app_name)
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            rightContent?.invoke() // invoke only if provided
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
