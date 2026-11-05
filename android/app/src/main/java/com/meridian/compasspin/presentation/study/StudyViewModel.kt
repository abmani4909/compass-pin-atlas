package com.meridian.compasspin.presentation.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class StudyUiState(
    val query: String = "",
    val places: List<Place> = emptyList()
) {
    val empty: Boolean get() = places.isEmpty()
}

class StudyViewModel : ViewModel() {

    private val bank: List<Place> = SampleData.places.sortedBy { it.name }

    private val _query = MutableStateFlow("")

    val state: StateFlow<StudyUiState> = _query
        .map { raw ->
            val needle = raw.trim()
            val matches = if (needle.isEmpty()) {
                bank
            } else {
                bank.filter { place -> place.name.contains(needle, ignoreCase = true) }
            }
            StudyUiState(query = raw, places = matches)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StudyUiState(places = bank)
        )

    fun onQueryChanged(value: String) {
        _query.value = value
    }

    fun onQueryCleared() {
        _query.value = ""
    }
}
