package com.meridian.compasspin.domain.usecase

import com.meridian.compasspin.domain.model.Place

class BuildTripUseCase(private val bank: List<Place>) {

    operator fun invoke(atlasId: String, clues: Int): List<Place> {
        val pool = if (atlasId == WORLD) bank else bank.filter { it.region == atlasId }
        val source = if (pool.size >= clues) pool else bank
        return source.shuffled().take(clues)
    }

    companion object {
        const val WORLD = "world"
    }
}
