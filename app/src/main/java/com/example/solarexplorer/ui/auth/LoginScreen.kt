package com.example.solarexplorer.ui.auth

//import androidx.compose.foundation.LocalIndication

//import androidx.compose.foundation.LocalIndication
//import androidx.compose.material3.LocalIndication

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(listOf(Color(0xFF0A0F1A), Color(0xFF0B3D91)))

    val loginState by vm.loginState.collectAsState(initial = UiState.Idle)

    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Success -> {
                delay(600)
                onLoginSuccess()
            }

            is UiState.Error -> {
                // do nothing
            }

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1724)),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AnimatedAppLogo(size = androidx.compose.ui.unit.Dp(110f), glowColor = Color(0xFF8A2BE2))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Welcome Back!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthInputField(
                    label = "Email",
                    value = vm.email.value,
                    onChange = { vm.email.value = it }
                )

                AuthPasswordField(
                    label = "Password",
                    value = vm.password.value,
                    onChange = { vm.password.value = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                when (loginState) {
                    is UiState.Loading -> {
                        Text(
                            text = "Logging in...",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }

                    is UiState.Error -> {
                        Text(
                            text = (loginState as UiState.Error).message,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }

                    is UiState.Success -> {
                        val message = if (vm.successMessage.value.isNotBlank())
                            vm.successMessage.value
                        else
                            (loginState as UiState.Success).message

                        Text(
                            text = message,
                            color = Color.Green,
                            fontSize = 14.sp
                        )
                    }

                    else -> {
                        if (vm.errorMessage.value.isNotBlank()) {
                            Text(
                                text = vm.errorMessage.value,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { vm.login() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F))
                ) {
                    Text(
                        text = "Login",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // FIXED: Add interactionSource + indication
                TextButton(
                    onClick = onRegisterClick
                ) {
                    Text(
                        text = "Don't have an account? Register",
                        color = Color.White
                    )
                }
            }
        }
    }
}
