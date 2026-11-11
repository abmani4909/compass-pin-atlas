package com.meridian.compasspin.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.PaperSurface

@Composable
fun PaperCard(
    modifier: Modifier = Modifier,
    containerColor: Color = PaperSurface,
    borderColor: Color = Hairline,
    radius: Int = 10,
    elevation: Int = 2,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(radius.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        modifier = modifier,
        content = content
    )
}
