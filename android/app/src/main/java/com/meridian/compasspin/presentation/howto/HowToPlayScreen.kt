package com.meridian.compasspin.presentation.howto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meridian.compasspin.core.ui.AppIcons
import com.meridian.compasspin.R
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.ui.PrimaryButton
import com.meridian.compasspin.core.ui.SecondaryButton
import com.meridian.compasspin.core.ui.SectionRule
import com.meridian.compasspin.data.sample.SampleData

@Composable
fun HowToPlayScreen(
    onPlay: (String) -> Unit,
    onMenu: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xE6EAF0E6),
                            Color(0xF2F2EAD8),
                            Color(0xE6EAF0E6)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(92.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.how_to_play),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 22.sp,
                color = InkText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            SectionRule()
            Spacer(modifier = Modifier.height(14.dp))

            Step(
                numeral = STEP_ONE,
                title = stringResource(R.string.howto_1_title),
                body = stringResource(R.string.howto_1_body)
            )
            Step(
                numeral = STEP_TWO,
                title = stringResource(R.string.howto_2_title),
                body = stringResource(R.string.howto_2_body)
            )
            Step(
                numeral = STEP_THREE,
                title = stringResource(R.string.howto_3_title),
                body = stringResource(R.string.howto_3_body)
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.howto_hint),
                style = MaterialTheme.typography.labelSmall,
                color = Land.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = { onPlay(SampleData.WORLD) },
                leadingIcon = AppIcons.PlayArrow,
                height = 56
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                leadingImage = R.drawable.icon_home,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun Step(numeral: String, title: String, body: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = numeral,
            style = MaterialTheme.typography.headlineMedium,
            color = Accent,
            modifier = Modifier.width(38.dp)
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(56.dp)
                .background(Hairline)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                color = InkText
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = Land
            )
        }
    }
}

private const val STEP_ONE = "1"
private const val STEP_TWO = "2"
private const val STEP_THREE = "3"
