package com.meridian.compasspin.domain.usecase

import com.meridian.compasspin.domain.model.Grade

class GradeTripUseCase {

    operator fun invoke(totalScore: Int): Grade =
        Grade.entries.firstOrNull { totalScore >= it.threshold } ?: Grade.D
}
