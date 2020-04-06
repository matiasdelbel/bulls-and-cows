package com.delbel.bullscows.game.gateway.model

import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ShiftDoTest {

    @Test
    fun `createFrom should create a new instance of the DO`() {
        val guessDo = ShiftDo.GuessDo(first = 1, second = 2, third = 3, fourth = 4)
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val answer = Answer(bulls = 2, cows = 1)
        val shift = Shift(attempt = 1, guess = guess, answer = answer, maxAttempts = 3)

        val shiftDo = ShiftDo.createFrom(id = GameId(id = 12), shift = shift)

        assertThat(shiftDo.gameId).isEqualTo(12)
        assertThat(shiftDo.attempt).isEqualTo(1)
        assertThat(shiftDo.bulls).isEqualTo(2)
        assertThat(shiftDo.cows).isEqualTo(1)
        assertThat(shiftDo.guess).isEqualTo(guessDo)
    }

    @Test
    fun `asModel should create a new instance of the business object`() {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val answer = Answer(bulls = 2, cows = 1)
        val expectedShift = Shift(attempt = 1, guess = guess, answer = answer, maxAttempts = 3)
        val shiftDo = ShiftDo.createFrom(id = GameId(id = 12), shift = expectedShift)

        val shift = shiftDo.asModel()

        assertThat(shift).isEqualTo(expectedShift)
    }
}