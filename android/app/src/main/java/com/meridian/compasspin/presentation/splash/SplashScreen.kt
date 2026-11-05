package com.meridian.compasspin.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.R
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.DarkGroundMid
import com.meridian.compasspin.core.theme.DarkGroundTop
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InverseText
import com.meridian.compasspin.core.theme.Land

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory())
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val entry by animateFloatAsState(
        targetValue = if (visible) 1f else 0.82f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = 220f)
    )
    val fade by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    val loop = rememberInfiniteTransition()
    val sweep by loop.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing))
    )
    val breathe by loop.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val glow by loop.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(DarkGroundTop, DarkGroundMid, DarkGroundTop))
            )
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.18f,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(
                    modifier = Modifier
                        .size(210.dp)
                        .scale(breathe)
                ) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val base = size.minDimension / 2f
                    listOf(0.92f, 0.74f, 0.56f).forEach { factor ->
                        drawCircle(
                            color = Hairline.copy(alpha = 0.25f),
                            radius = base * factor,
                            center = center,
                            style = Stroke(width = 2f)
                        )
                    }
                }
                Canvas(
                    modifier = Modifier
                        .size(180.dp)
                        .rotate(sweep)
                ) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = size.minDimension / 2f
                    drawCircle(
                        color = Accent.copy(alpha = 0.45f),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 3.5f)
                    )
                    drawLine(
                        color = Accent.copy(alpha = 0.55f),
                        start = Offset(center.x, center.y - radius),
                        end = Offset(center.x, center.y - radius * 0.62f),
                        strokeWidth = 5f
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(136.dp)
                        .scale(entry)
                        .alpha(glow * fade)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall,
                color = InverseText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(fade)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = Hairline,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(fade)
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                color = Accent,
                trackColor = Land.copy(alpha = 0.4f),
                modifier = Modifier
                    .width(180.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_status),
                style = MaterialTheme.typography.labelSmall,
                color = Hairline.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
