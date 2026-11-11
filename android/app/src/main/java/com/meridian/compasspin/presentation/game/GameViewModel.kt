package com.meridian.compasspin.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridian.compasspin.core.config.GameConfig
import com.meridian.compasspin.core.map.MapProjection
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.model.ClueResult
import com.meridian.compasspin.domain.model.LatLon
import com.meridian.compasspin.domain.model.MapCamera
import com.meridian.compasspin.domain.model.Place
import com.meridian.compasspin.domain.model.TripResult
import com.meridian.compasspin.domain.repository.SettingsRepository
import com.meridian.compasspin.domain.usecase.BuildTripUseCase
import com.meridian.compasspin.domain.usecase.CalculateDistanceUseCase
import com.meridian.compasspin.domain.usecase.GradeTripUseCase
import com.meridian.compasspin.domain.usecase.ScoreGuessUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GameViewModel(
    private val atlasId: String,
    settingsRepository: SettingsRepository,
    private val buildTrip: BuildTripUseCase,
    private val calculateDistance: CalculateDistanceUseCase,
    private val scoreGuess: ScoreGuessUseCase,
    private val gradeTrip: GradeTripUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<TripResult>(Channel.BUFFERED)
    val events: Flow<TripResult> = _events.receiveAsFlow()

    private val clues: List<Place> = buildTrip(atlasId, GameConfig.CLUES_PER_TRIP)
    private val atlas = SampleData.atlasOf(atlasId)
    private val falloff =
        if (atlas.regional) GameConfig.REGION_FALLOFF_KM else GameConfig.WORLD_FALLOFF_KM

    private var viewWidth = 0f
    private var viewHeight = 0f
    private var arcJob: Job? = null
    private var cameraJob: Job? = null

    init {
        val settings = settingsRepository.settings.value
        _state.value = GameUiState(
            atlasId = atlasId,
            clueIndex = 0,
            totalClues = clues.size,
            place = clues.firstOrNull(),
            camera = homeCamera(),
            showBorders = !settings.hardBorders,
            useMiles = settings.useMiles
        )
    }

    fun onTransform(panX: Float, panY: Float, zoom: Float, width: Float, height: Float) {
        viewWidth = width
        viewHeight = height
        if (!_state.value.interactive) return
        cameraJob?.cancel()
        val zoomed = MapProjection.zoom(_state.value.camera, zoom, width, height)
        val panned = MapProjection.pan(zoomed, panX, panY, width, height)
        _state.update { it.copy(camera = panned) }
    }

    fun onMapTap(x: Float, y: Float, width: Float, height: Float) {
        viewWidth = width
        viewHeight = height
        if (!_state.value.interactive) return
        val point = MapProjection.toLatLon(x, y, width, height, _state.value.camera)
        _state.update { it.copy(pin = point, phase = GamePhase.PINNED) }
    }

    fun onRecentre() {
        if (!_state.value.interactive) return
        animateCamera(homeCamera(), GameConfig.FRAME_RECENTRE_MS)
    }

    fun onConfirmPin() {
        val current = _state.value
        val guess = current.pin ?: return
        val place = current.place ?: return
        if (current.phase != GamePhase.PINNED) return

        val truth = place.position
        val distance = calculateDistance(guess, truth)
        val points = scoreGuess(distance, falloff, GameConfig.BULLSEYE_KM)
        val bullseye = distance <= GameConfig.BULLSEYE_KM
        val entry = ClueResult(
            placeId = place.id,
            placeName = place.name,
            category = place.category,
            distanceKm = distance.roundToInt(),
            points = points,
            fact = place.fact,
            bullseye = bullseye
        )

        _state.update {
            it.copy(
                phase = GamePhase.REVEALING,
                truth = truth,
                distanceKm = entry.distanceKm,
                pointsEarned = points,
                bullseye = bullseye,
                score = it.score + points,
                results = it.results + entry,
                arcFraction = 0f
            )
        }

        if (viewWidth > 0f && viewHeight > 0f) {
            animateCamera(
                MapProjection.frame(guess, truth, viewWidth, viewHeight),
                GameConfig.FRAME_FOCUS_MS
            )
        }

        arcJob?.cancel()
        arcJob = viewModelScope.launch {
            val started = System.currentTimeMillis()
            while (isActive) {
                val elapsed = (System.currentTimeMillis() - started).toFloat()
                val fraction = (elapsed / GameConfig.ARC_DURATION_MS.toFloat()).coerceIn(0f, 1f)
                _state.update { it.copy(arcFraction = fraction) }
                if (fraction >= 1f) break
                delay(GameConfig.FRAME_STEP_MS)
            }
            _state.update { it.copy(phase = GamePhase.REVEALED, arcFraction = 1f) }
        }
    }

    fun onAdvance() {
        val current = _state.value
        if (current.phase != GamePhase.REVEALED) return
        if (current.lastClue) {
            emitTrip(current.results, current.score)
            return
        }
        val nextIndex = current.clueIndex + 1
        cameraJob?.cancel()
        arcJob?.cancel()
        _state.update {
            it.copy(
                clueIndex = nextIndex,
                place = clues.getOrNull(nextIndex),
                phase = GamePhase.GUESSING,
                pin = null,
                truth = null,
                arcFraction = 0f,
                distanceKm = 0,
                pointsEarned = 0,
                bullseye = false,
                camera = homeCamera()
            )
        }
    }

    fun onPauseRequested() {
        if (_state.value.abandonVisible) return
        _state.update { it.copy(abandonVisible = true) }
    }

    fun onAbandonDismissed() {
        _state.update { it.copy(abandonVisible = false) }
    }

    fun onAbandonConfirmed() {
        val current = _state.value
        _state.update { it.copy(abandonVisible = false) }
        emitTrip(current.results, current.score)
    }

    fun pause() {
        arcJob?.cancel()
        cameraJob?.cancel()
    }

    private fun emitTrip(results: List<ClueResult>, score: Int) {
        pause()
        val trip = TripResult(
            tripId = System.currentTimeMillis(),
            atlasId = atlasId,
            totalScore = score,
            grade = gradeTrip(score),
            clues = results
        )
        viewModelScope.launch { _events.send(trip) }
    }

    private fun homeCamera(): MapCamera =
        MapCamera(scale = atlas.scale, centerLon = atlas.centerLon, centerLat = atlas.centerLat)

    private fun animateCamera(target: MapCamera, durationMs: Long) {
        cameraJob?.cancel()
        cameraJob = viewModelScope.launch {
            val from = _state.value.camera
            val started = System.currentTimeMillis()
            while (isActive) {
                val elapsed = (System.currentTimeMillis() - started).toFloat()
                val raw = (elapsed / durationMs.toFloat()).coerceIn(0f, 1f)
                val eased = raw * raw * (3f - 2f * raw)
                val step = MapCamera(
                    scale = from.scale + (target.scale - from.scale) * eased,
                    centerLon = from.centerLon + (target.centerLon - from.centerLon) * eased,
                    centerLat = from.centerLat + (target.centerLat - from.centerLat) * eased
                )
                _state.update { it.copy(camera = step) }
                if (raw >= 1f) break
                delay(GameConfig.FRAME_STEP_MS)
            }
        }
    }
}
