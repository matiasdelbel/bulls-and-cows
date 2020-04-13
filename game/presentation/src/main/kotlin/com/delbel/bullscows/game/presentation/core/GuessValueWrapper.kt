package com.delbel.bullscows.game.presentation.core

import android.widget.Button
import javax.inject.Inject

internal class GuessValueWrapper @Inject constructor() {

    companion object {
        private val POSSIBLE_VALUES = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    }

    fun wrapNext(button: Button) {
        val value = unwrap(button)
        check(value >= 0 && value <= POSSIBLE_VALUES.size + 1)

        button.text = "${nextValue(current = value)}"
    }

    fun unwrap(button: Button) = button.text.toString().toInt()

    private fun nextValue(current: Int): Int {
        val candidate = current + 1
        return POSSIBLE_VALUES.getOrElse(candidate) { POSSIBLE_VALUES[0] }
    }
}