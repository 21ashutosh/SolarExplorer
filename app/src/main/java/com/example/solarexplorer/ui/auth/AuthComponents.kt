package com.example.solarexplorer.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.material3.LocalIndication
import com.example.solarexplorer.R
import androidx.compose.foundation.LocalIndication

/**
 * -------------------------------
 *   APP LOGO AT TOP CENTER
 * -------------------------------
 */
@Composable
fun AuthLogo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.galaxy),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * -------------------------------
 *  Auth Input (Email / Name / Text)
 * -------------------------------
 */
@Composable
fun AuthInputField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    Column(Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label, color = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD54F),
                unfocusedBorderColor = Color.LightGray,
                errorBorderColor = Color.Red,
                cursorColor = Color(0xFFFFD54F),
                focusedLabelColor = Color(0xFFFFD54F),
                unfocusedLabelColor = Color.LightGray
            ),
            isError = isError
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
    }
}

/**
 * -------------------------------
 *  Auth Password Field
 * -------------------------------
 */
@Composable
fun AuthPasswordField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    var isVisible by remember { mutableStateOf(false) }

    val interaction = remember { MutableInteractionSource() }

    Column(Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            label = { Text(label, color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "Toggle Password",
                    tint = Color.White,
                    modifier = Modifier.clickable(
                        interactionSource = interaction,
                        indication = LocalIndication.current
                    ) {
                        isVisible = !isVisible
                    }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD54F),
                unfocusedBorderColor = Color.LightGray,
                errorBorderColor = Color.Red,
                cursorColor = Color(0xFFFFD54F),
                focusedLabelColor = Color(0xFFFFD54F),
                unfocusedLabelColor = Color.LightGray
            ),
            isError = isError
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
    }
}

/**
 * -------------------------------
 *  ANIMATED APP LOGO
 * -------------------------------
 */
@Composable
fun AnimatedAppLogo(
    size: Dp = 110.dp,
    glowColor: Color = Color(0xFF8A2BE2)
) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size * scale)
    ) {
        // Glow behind logo
        Box(
            modifier = Modifier
                .size(size * 1.4f * scale)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(glowColor.copy(alpha = glowAlpha), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        // Logo
        Image(
            painter = painterResource(id = R.drawable.galaxy),
            contentDescription = "App Logo",
            modifier = Modifier.size(size * scale)
        )
    }
}
