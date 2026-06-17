package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun VarnikaLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    animate: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logoGlow")
    
    val glowProgress by if (animate) {
        infiniteTransition.animateFloat(
            initialValue = 0.92f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    val drawPercent by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1200, easing = LinearOutSlowInEasing),
        label = "drawPercent"
    )

    Box(
        modifier = modifier
            .size(size)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.toPx()
            val h = size.toPx()
            val cx = w / 2
            val cy = h / 2

            val scale = glowProgress
            val scaleX: (Float) -> Float = { x -> cx + (x - cx) * scale }
            val scaleY: (Float) -> Float = { y -> cy + (y - cy) * scale }

            // Define Isometric Room Panels
            val leftWallPath = Path().apply {
                moveTo(scaleX(w * 0.25f), scaleY(h * 0.20f)) // Left Outer Top
                lineTo(scaleX(w * 0.5f), scaleY(h * 0.75f))   // Bottom Corner Joint
                lineTo(scaleX(w * 0.5f), scaleY(h * 0.45f))  // Center Vertical Joint
                lineTo(scaleX(w * 0.25f), scaleY(h * 0.20f)) // Back to Outer
                close()
            }

            val rightWallPath = Path().apply {
                moveTo(scaleX(w * 0.75f), scaleY(h * 0.20f)) // Right Outer Top
                lineTo(scaleX(w * 0.5f), scaleY(h * 0.75f))   // Bottom Corner Joint
                lineTo(scaleX(w * 0.5f), scaleY(h * 0.45f))  // Center Vertical Joint
                lineTo(scaleX(w * 0.75f), scaleY(h * 0.20f)) // Back to Outer
                close()
            }

            val ceilingIndicatorPath = Path().apply {
                moveTo(scaleX(w * 0.5f), scaleY(h * 0.45f))  // Center Wall Joint
                lineTo(scaleX(w * 0.25f), scaleY(h * 0.20f)) // Left Outer Top
                lineTo(scaleX(w * 0.5f), scaleY(h * 0.08f))  // Soft Room Back Ceiling Corner
                lineTo(scaleX(w * 0.75f), scaleY(h * 0.20f)) // Right Outer Top
                close()
            }

            // Draw Floor/Roof Backplate with Warm Cream soft highlight
            drawPath(
                path = ceilingIndicatorPath,
                color = VarnikaCreamSecondary.copy(alpha = 0.12f)
            )

            // Left Wall in Sage Green (Primary Logo Color)
            drawPath(
                path = leftWallPath,
                brush = Brush.verticalGradient(
                    colors = listOf(VarnikaSagePrimary, VarnikaSagePrimary.copy(alpha = 0.65f))
                )
            )

            // Right Wall in Warm Cream Beige (Secondary Logo Color)
            drawPath(
                path = rightWallPath,
                brush = Brush.verticalGradient(
                    colors = listOf(VarnikaCreamSecondary, VarnikaCreamSecondary.copy(alpha = 0.65f))
                )
            )

            // Outline path to trace the "V" perfectly for striking modern feel
            val vOutlinePath = Path().apply {
                moveTo(scaleX(w * 0.25f), scaleY(h * 0.20f)) // Top-left
                lineTo(scaleX(w * 0.5f), scaleY(h * 0.75f))   // Bottom-tip
                lineTo(scaleX(w * 0.75f), scaleY(h * 0.20f)) // Top-right
            }
            
            // Draw elegant, high-contrast V stroke outline (Soft Ivory)
            drawPath(
                path = vOutlinePath,
                color = VarnikaIvoryText.copy(alpha = 0.9f * drawPercent),
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Interactive middle-line room seam/glow indicator representing "AI Intelligence"
            drawLine(
                color = VarnikaIvoryText.copy(alpha = 0.6f),
                start = Offset(scaleX(w * 0.5f), scaleY(h * 0.45f)),
                end = Offset(scaleX(w * 0.5f), scaleY(h * 0.75f)),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}
