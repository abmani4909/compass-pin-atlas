package com.meridian.compasspin.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.AccentLight
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.InverseText

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Int = 56,
    leadingIcon: ImageVector? = null,
    stateLabel: String = ""
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 900f)
    )
    val shape = RoundedCornerShape(10.dp)
    val fill = if (enabled) {
        Brush.horizontalGradient(listOf(Accent, AccentLight))
    } else {
        Brush.horizontalGradient(listOf(Hairline, Hairline))
    }
    val labelColor = if (enabled) InverseText else InkText.copy(alpha = 0.38f)

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = labelColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = labelColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (enabled) 3.dp else 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .scale(scale)
            .then(
                if (stateLabel.isEmpty()) Modifier
                else Modifier.semantics { stateDescription = stateLabel }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = fill, shape = shape),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = labelColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = text,
                    style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                    color = labelColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
