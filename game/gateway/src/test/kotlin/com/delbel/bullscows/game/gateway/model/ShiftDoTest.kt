package com.delbel.bullscows.game.gateway.model

import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ShiftDoTest {

    @Test
    fun `asModel should create a new instance of the business object`() {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val expectedShift = Shift(attempt = 1, guess = guess, answer =  Answer(bulls = 2, cows = 1), maxAttempts = 3)
        val guessDo = ShiftDo.GuessDo(first = 1, second = 2, third = 3, fourth = 4)
        val shiftDo = ShiftDo(gameId = 12, attempt = 1, bulls = 2, cows = 1, maxAttempts = 3, guess = guessDo)

        val shift = shiftDo.asModel()

        assertThat(shift).isEqualTo(expectedShift)
    }
}