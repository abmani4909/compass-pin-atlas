package com.meridian.compasspin.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.theme.PaperSurface

@Composable
fun AtlasTile(
    title: String,
    badge: String,
    earned: Boolean,
    visible: Boolean,
    delayMillis: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appear by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 340, delayMillis = delayMillis, easing = FastOutSlowInEasing)
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PaperSurface),
        border = BorderStroke(1.dp, Hairline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .height(78.dp)
            .alpha(appear)
            .clickable(onClick = onClick)
            .semantics { stateDescription = badge }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp,
                color = InkText
            )
            Box(
                modifier = Modifier
                    .background(
                        color = if (earned) Accent.copy(alpha = 0.10f) else Land.copy(alpha = 0.06f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (earned) Accent else Land.copy(alpha = 0.6f)
                )
            }
        }
    }
}
