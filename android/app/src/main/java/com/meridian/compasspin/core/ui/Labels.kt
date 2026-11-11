package com.meridian.compasspin.core.ui

import com.meridian.compasspin.R
import com.meridian.compasspin.data.sample.SampleData
import com.meridian.compasspin.domain.model.Category

object Labels {

    fun atlasName(atlasId: String): Int = when (atlasId) {
        SampleData.EUROPE -> R.string.region_europe
        SampleData.AFRICA -> R.string.region_africa
        SampleData.ASIA -> R.string.region_asia
        SampleData.AMERICAS -> R.string.region_americas
        SampleData.OCEANIA -> R.string.region_oceania
        SampleData.POLAR -> R.string.region_polar
        else -> R.string.region_world
    }

    fun category(category: Category): Int = when (category) {
        Category.RIVER -> R.string.category_river
        Category.MOUNTAIN_RANGE -> R.string.category_mountain
        Category.CAPITAL -> R.string.category_capital
        Category.NATURAL_WONDER -> R.string.category_wonder
    }

    fun categorySprite(category: Category): Int = when (category) {
        Category.RIVER -> R.drawable.sprite_2
        Category.MOUNTAIN_RANGE -> R.drawable.sprite_3
        Category.CAPITAL -> R.drawable.sprite_4
        Category.NATURAL_WONDER -> R.drawable.sprite_5
    }

    fun groupNumber(value: Int): String {
        val digits = value.toString()
        if (digits.length <= 3) return digits
        val builder = StringBuilder()
        digits.forEachIndexed { index, char ->
            if (index > 0 && (digits.length - index) % 3 == 0) builder.append(' ')
            builder.append(char)
        }
        return builder.toString()
    }
}
