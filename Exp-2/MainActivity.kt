package com.example.cellulargameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color(0xFF1B241E),
                    surface = Color(0xFF4E5933)
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF1B241E)) {
                    CellularGameApp()
                }
            }
        }
    }
}

@Composable
fun CellularGameApp() {
    var score by remember { mutableIntStateOf(0) }
    var iParam by remember { mutableIntStateOf(1) }
    var jParam by remember { mutableIntStateOf(2) }
    var userInput by remember { mutableStateOf("") }
    var revealed by remember { mutableStateOf(false) }

    val actualN = (iParam * iParam) + (iParam * jParam) + (jParam * jParam)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cellular Game", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Score: $score", fontSize = 18.sp, color = Color.LightGray)

        Spacer(modifier = Modifier.height(16.dp))

        // UI Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4E5933)),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Locate the Co-Channel Cells", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Move i = $iParam, turn 60°, move j = $jParam", color = Color.White, modifier = Modifier.padding(top = 8.dp))

                OutlinedTextField(
                    value = userInput,
                    onValueChange = { if (it.length <= 2) userInput = it },
                    label = { Text("Enter N", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.padding(top = 12.dp).width(120.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )

                Button(
                    onClick = {
                        if (!revealed) {
                            if (userInput.toIntOrNull() == actualN) score += 10
                            revealed = true
                        } else {
                            iParam = kotlin.random.Random.nextInt(1, 3)
                            jParam = kotlin.random.Random.nextInt(1, 3)
                            userInput = ""
                            revealed = false
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(0.7f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2842B5))
                ) {
                    Text(if (!revealed) "Check Answer & Reveal" else "Next Level")
                }
            }
        }

        // --- CHANGES START HERE ---
        // 1. Added a larger Spacer to push the grid further down from the top card
        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter // Aligning to top of the remaining space
        ) {
            // 2. Increased Canvas size from 320.dp to 400.dp
            Canvas(modifier = Modifier.size(800.dp)) {
                // 3. Increased hexRadius from 24f to 35f for a "Big" hexagon look
                val hexRadius = 35f
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Draw background grid
                for (q in -3..3) {
                    for (r in -3..3) {
                        if (kotlin.math.abs(q + r + (-q - r)) <= 6) {
                            val x = cx + hexRadius * kotlin.math.sqrt(3f) * (q + r / 2f)
                            val y = cy + hexRadius * 1.5f * r
                            drawHexagon(x, y, hexRadius, Color.Gray.copy(alpha = 0.2f))
                        }
                    }
                }

                // Reference Cell (Gold)
                drawHexagon(cx, cy, hexRadius, Color(0xFFFFD700))

                // Co-Channels (Teal)
                if (revealed) {
                    val qBase = iParam + jParam
                    val rBase = -jParam
                    val coords = listOf(
                        Pair(qBase, rBase), Pair(-rBase, qBase + rBase), Pair(-qBase - rBase, qBase),
                        Pair(-qBase, -rBase), Pair(rBase, -qBase - rBase), Pair(qBase + rBase, -qBase)
                    )
                    coords.forEach { (q, r) ->
                        val tx = cx + hexRadius * kotlin.math.sqrt(3f) * (q + r / 2f)
                        val ty = cy + hexRadius * 1.5f * r
                        drawHexagon(tx, ty, hexRadius, Color(0xFF1DE9B6))
                    }
                }
            }
        }
    }
}

fun DrawScope.drawHexagon(x: Float, y: Float, radius: Float, color: Color) {
    val path = Path()
    for (i in 0..5) {
        val angle = (60 * i - 30) * PI / 180f
        val px = x + radius * cos(angle).toFloat()
        val py = y + radius * sin(angle).toFloat()
        if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
    }
    path.close()
    drawPath(path, color)
    drawPath(path, Color.Black.copy(0.3f), style = Stroke(1.5f))
}