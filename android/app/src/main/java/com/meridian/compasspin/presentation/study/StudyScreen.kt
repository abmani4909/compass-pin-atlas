package com.meridian.compasspin.presentation.study

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.meridian.compasspin.core.ui.AppIcons
import com.meridian.compasspin.R
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.theme.Accent
import com.meridian.compasspin.core.theme.Hairline
import com.meridian.compasspin.core.theme.InkText
import com.meridian.compasspin.core.theme.Land
import com.meridian.compasspin.core.theme.PaperRaised
import com.meridian.compasspin.core.ui.Labels
import com.meridian.compasspin.core.ui.PaperCard
import com.meridian.compasspin.core.ui.SecondaryButton
import com.meridian.compasspin.core.ui.SectionRule
import com.meridian.compasspin.domain.model.Place

@Composable
fun StudyScreen(onMenu: () -> Unit) {
    val studyViewModel: StudyViewModel = viewModel(factory = AppViewModelFactory())
    val state by studyViewModel.state.collectAsStateWithLifecycle()
    StudyContent(
        state = state,
        onQueryChanged = { value -> studyViewModel.onQueryChanged(value) },
        onQueryCleared = { studyViewModel.onQueryCleared() },
        onMenu = onMenu
    )
}

@Composable
private fun StudyContent(
    state: StudyUiState,
    onQueryChanged: (String) -> Unit,
    onQueryCleared: () -> Unit,
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
                            Color(0xF2F2EAD8),
                            Color(0xE6EAF0E6),
                            Color(0xF2DCE6D6)
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
                        text = stringResource(R.string.place_glossary),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 22.sp,
                        color = InkText
                    )
                    Text(
                        text = stringResource(R.string.glossary_sub, state.places.size),
                        style = MaterialTheme.typography.labelSmall,
                        color = Land
                    )
                }
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(42.dp)
                )
            }
            SectionRule()
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChanged,
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_places),
                        style = MaterialTheme.typography.labelMedium,
                        color = Land.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = AppIcons.Search,
                        contentDescription = null,
                        tint = Land,
                        modifier = Modifier.size(22.dp)
                    )
                },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = onQueryCleared, modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = AppIcons.Close,
                                contentDescription = stringResource(R.string.cancel),
                                tint = Accent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PaperRaised.copy(alpha = 0.92f),
                    unfocusedContainerColor = PaperRaised.copy(alpha = 0.78f),
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Hairline,
                    focusedTextColor = InkText,
                    unfocusedTextColor = InkText,
                    cursorColor = Accent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (state.empty) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(84.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = stringResource(R.string.glossary_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Land,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = state.places, key = { place -> place.id }) { place ->
                        GlossaryRow(place = place)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
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
private fun GlossaryRow(place: Place) {
    PaperCard(modifier = Modifier.fillMaxWidth(), radius = 8, elevation = 1) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Labels.categorySprite(place.category)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp,
                    color = InkText,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(
                        R.string.glossary_meta,
                        stringResource(Labels.atlasName(place.region)),
                        stringResource(Labels.category(place.category))
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = Land.copy(alpha = 0.85f),
                    maxLines = 1
                )
            }
        }
    }
}
