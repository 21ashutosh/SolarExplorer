package com.example.solarexplorer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(
    score: Int,
    total: Int,
    onRetry: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage = score.toFloat() / total.toFloat()

    val message = when {
        score == total -> "🎉 Congratulations!!! You got full marks!"
        percentage >= 0.5f -> "✨ Nice!! You scored good marks!"
        else -> "🙂 Good try! Do it once again!"
    }

    val backgroundColors = listOf(
        Color(0xFF0F2027),
        Color(0xFF203A43),
        Color(0xFF2C5364)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(backgroundColors))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = message,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Score",
            fontSize = 20.sp,
            color = Color.White
        )

        Text(
            text = "$score / $total",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Yellow
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Retry Quiz", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackHome,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Back to Home", fontSize = 18.sp, color = Color.White)
        }
    }
}
