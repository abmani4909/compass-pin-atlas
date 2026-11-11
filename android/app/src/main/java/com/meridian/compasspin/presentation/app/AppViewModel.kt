package com.meridian.compasspin.presentation.app

import androidx.lifecycle.ViewModel
import com.meridian.compasspin.core.navigation.Screen
import com.meridian.compasspin.domain.model.TripResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val stack = ArrayDeque<Screen>()
    private var sessions = 0L

    fun openMenu() {
        stack.clear()
        _screen.value = Screen.Menu
        publish()
    }

    fun openTrip(atlasId: String) {
        sessions += 1
        push(Screen.Game(atlasId, sessions))
    }

    fun openPassport() = push(Screen.Passport)

    fun openWeakSpots() = push(Screen.WeakSpots)

    fun openStudy() = push(Screen.Study)

    fun openHowToPlay() = push(Screen.HowToPlay)

    fun openSettings() = push(Screen.Settings)

    fun finishTrip(result: TripResult) {
        stack.clear()
        stack.addLast(Screen.Menu)
        _screen.value = Screen.GameOver(result)
        publish()
    }

    fun replayTrip(atlasId: String) {
        stack.clear()
        stack.addLast(Screen.Menu)
        sessions += 1
        _screen.value = Screen.Game(atlasId, sessions)
        publish()
    }

    fun back(): Boolean {
        val previous = stack.removeLastOrNull() ?: return false
        _screen.value = previous
        publish()
        return true
    }

    private fun push(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) stack.addLast(current)
        _screen.value = target
        publish()
    }

    private fun publish() {
        _canGoBack.value = stack.isNotEmpty()
    }
}
