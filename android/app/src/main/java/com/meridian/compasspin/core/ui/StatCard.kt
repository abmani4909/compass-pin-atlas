package com.meridian.compasspin.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.PaperSurface

@Composable
fun StatCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
    containerColor: Color = PaperSurface,
    labelColor: Color = InkText.copy(alpha = 0.6f),
    height: Int = 70
) {
    PaperCard(
        modifier = modifier.height(height.dp),
        containerColor = containerColor,
        borderColor = Hairline,
        radius = 8,
        elevation = 0
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(valueColor, CircleShape)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 21.sp,
                color = valueColor,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = labelColor,
                textAlign = TextAlign.Start
            )
        }
    }
}
