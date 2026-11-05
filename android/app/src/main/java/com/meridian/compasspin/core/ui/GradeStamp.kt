package com.meridian.compasspin.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline

@Composable
fun GradeStamp(
    grade: String,
    caption: String,
    modifier: Modifier = Modifier,
    size: Int = 120,
    animateIn: Boolean = true,
    inkColor: Color = Accent,
    emptyColor: Color = Hairline,
    earned: Boolean = true
) {
    var landed by remember { mutableStateOf(!animateIn) }
    LaunchedEffect(animateIn) { landed = true }

    val scale by animateFloatAsState(
        targetValue = if (landed) 1f else 1.9f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 340f)
    )
    val rotation by animateFloatAsState(
        targetValue = if (landed) -9f else -18f,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
    )
    val appear by animateFloatAsState(
        targetValue = if (landed) 1f else 0f,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
    )

    val ink = if (earned) inkColor else emptyColor

    Box(
        modifier = modifier
            .size(size.dp)
            .scale(scale)
            .rotate(rotation)
            .alpha(appear),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = this.size.minDimension / 2f
            drawCircle(
                color = ink,
                radius = radius - 3f,
                style = Stroke(width = 6f)
            )
            drawCircle(
                color = ink.copy(alpha = 0.7f),
                radius = radius * 0.78f,
                style = Stroke(width = 3f)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = grade,
                style = MaterialTheme.typography.displaySmall,
                fontSize = (size * 0.42f).sp,
                fontWeight = FontWeight.Bold,
                color = ink
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = ink.copy(alpha = 0.85f)
            )
        }
    }
}
