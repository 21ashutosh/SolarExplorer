// app/src/main/java/com/example/solarexplorer/ui/auth/UiState.kt
package com.example.solarexplorer.ui.auth

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}
