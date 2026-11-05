package com.meridian.compasspin.presentation.passport

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.R
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.ui.GradeStamp
import com.meridian.compasspin.core.ui.Labels
import com.meridian.compasspin.core.ui.PaperCard
import com.meridian.compasspin.core.ui.SecondaryButton
import com.meridian.compasspin.core.ui.SectionRule

@Composable
fun PassportScreen(onMenu: () -> Unit) {
    val passportViewModel: PassportViewModel = viewModel(factory = AppViewModelFactory())
    val state by passportViewModel.state.collectAsStateWithLifecycle()
    PassportContent(state = state, onMenu = onMenu)
}

@Composable
private fun PassportContent(state: PassportUiState, onMenu: () -> Unit) {
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
                            Color(0xF2F2EAD8),
                            Color(0xE6EAF0E6),
                            Color(0xF2F2EAD8)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.passport),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                        color = InkText
                    )
                    Text(
                        text = stringResource(R.string.stamp_value, state.earned, state.total),
                        style = MaterialTheme.typography.labelSmall,
                        color = Land
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
            }
            SectionRule()
            Spacer(modifier = Modifier.height(16.dp))

            val notVisited = stringResource(R.string.locked)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.stamps.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        row.forEach { entry ->
                            PaperCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(120.dp),
                                radius = 8,
                                elevation = 1
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    GradeStamp(
                                        grade = entry.grade?.letter ?: EMPTY_GRADE,
                                        caption = stringResource(Labels.atlasName(entry.atlasId)),
                                        size = 70,
                                        animateIn = false,
                                        earned = entry.grade != null
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (entry.grade == null) {
                                            notVisited
                                        } else {
                                            stringResource(
                                                R.string.best_score,
                                                Labels.groupNumber(entry.score)
                                            )
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (entry.grade == null) {
                                            Hairline
                                        } else {
                                            Land
                                        },
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                leadingImage = R.drawable.icon_home,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

private const val EMPTY_GRADE = "-"
