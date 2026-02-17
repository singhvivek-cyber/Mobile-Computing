package com.example.drawingcanvasapp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class LinePath(
    val path: Path,          // Stores the actual lines/curves you draw
    val color: Color,        // Stores the color selected for this specific stroke
    val strokeWidth: Float   // Stores how thick the line should be
)