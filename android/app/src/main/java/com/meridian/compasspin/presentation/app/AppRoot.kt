package com.meridian.compasspin.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meridian.compasspin.core.di.AppViewModelFactory
import com.meridian.compasspin.core.navigation.Screen
import com.meridian.compasspin.core.theme.Paper
import com.meridian.compasspin.presentation.game.GameScreen
import com.meridian.compasspin.presentation.gameover.GameOverScreen
import com.meridian.compasspin.presentation.howto.HowToPlayScreen
import com.meridian.compasspin.presentation.menu.MenuScreen
import com.meridian.compasspin.presentation.passport.PassportScreen
import com.meridian.compasspin.presentation.settings.SettingsScreen
import com.meridian.compasspin.presentation.splash.SplashScreen
import com.meridian.compasspin.presentation.study.StudyScreen
import com.meridian.compasspin.presentation.weakspots.WeakSpotsScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory())
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(tween(360)) + slideInVertically(tween(360)) { it / 14 } togetherWith
                    fadeOut(tween(240))
            },
            modifier = Modifier.fillMaxSize()
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(onFinished = { appViewModel.openMenu() })
                is Screen.Menu -> MenuScreen(
                    onPlay = { atlasId -> appViewModel.openTrip(atlasId) },
                    onPassport = { appViewModel.openPassport() },
                    onWeakSpots = { appViewModel.openWeakSpots() },
                    onStudy = { appViewModel.openStudy() },
                    onHowToPlay = { appViewModel.openHowToPlay() },
                    onSettings = { appViewModel.openSettings() }
                )
                is Screen.Game -> GameScreen(
                    atlasId = current.atlasId,
                    session = current.session,
                    onFinished = { result -> appViewModel.finishTrip(result) },
                    onExit = { appViewModel.openMenu() }
                )
                is Screen.GameOver -> GameOverScreen(
                    result = current.result,
                    onPlayAgain = { appViewModel.replayTrip(current.result.atlasId) },
                    onMenu = { appViewModel.openMenu() }
                )
                is Screen.Passport -> PassportScreen(onMenu = { appViewModel.openMenu() })
                is Screen.WeakSpots -> WeakSpotsScreen(
                    onPlay = { appViewModel.openTrip(it) },
                    onMenu = { appViewModel.openMenu() }
                )
                is Screen.Study -> StudyScreen(onMenu = { appViewModel.openMenu() })
                is Screen.HowToPlay -> HowToPlayScreen(
                    onPlay = { appViewModel.openTrip(it) },
                    onMenu = { appViewModel.openMenu() }
                )
                is Screen.Settings -> SettingsScreen(onMenu = { appViewModel.openMenu() })
            }
        }
    }
}
