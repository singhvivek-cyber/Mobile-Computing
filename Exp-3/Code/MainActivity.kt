package com.example.drawingcanvasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DrawingScreen()
            }
        }
    }
}

@Composable
fun DrawingScreen() {
    val paths = remember { mutableStateListOf<LinePath>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var currentColor by remember { mutableStateOf(Color(0xFF1DE9B6)) } // Teal
    var strokeWidth by remember { mutableFloatStateOf(10f) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Brush.radialGradient(listOf(Color(0xFF2C3E2D), Color(0xFF1B241E))))) { //

        // Main Canvas Area
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPath = Path().apply { moveTo(offset.x, offset.y) }
                        },
                        onDrag = { change, _ ->
                            currentPath?.lineTo(change.position.x, change.position.y)
                            val temp = currentPath
                            currentPath = null
                            currentPath = temp
                        },
                        onDragEnd = {
                            currentPath?.let { paths.add(LinePath(it, currentColor, strokeWidth)) }
                            currentPath = null
                        }
                    )
                }
        ) {
            paths.forEach { line ->
                drawPath(path = line.path, color = line.color, style = Stroke(line.strokeWidth, cap = StrokeCap.Round))
            }
            currentPath?.let {
                drawPath(path = it, color = currentColor, style = Stroke(strokeWidth, cap = StrokeCap.Round))
            }
        }

        // Floating Modern Toolbar
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .shadow(20.dp, RoundedCornerShape(30.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4E5933).copy(alpha = 0.9f)), //
            shape = RoundedCornerShape(30.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Color Selection
                listOf(Color.Red, Color(0xFF1DE9B6), Color(0xFFFFD700), Color.White).forEach { color ->
                    ColorCircle(color, isSelected = currentColor == color) { currentColor = color }
                }

                VerticalDivider(modifier = Modifier.height(30.dp), color = Color.White.copy(0.2f))

                // Action Buttons
                IconButton(onClick = { if (paths.isNotEmpty()) paths.removeAt(paths.size - 1) }) {
                    Icon(Icons.Default.Refresh, "Undo", tint = Color.White)
                }

                IconButton(onClick = { paths.clear() }) {
                    Icon(Icons.Default.Delete, "Clear", tint = Color(0xFFFF5252))
                }
            }
        }
    }
}

@Composable
fun ColorCircle(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = Color.White,
                shape = CircleShape
            )
            .clickable { onClick() }
    )
}