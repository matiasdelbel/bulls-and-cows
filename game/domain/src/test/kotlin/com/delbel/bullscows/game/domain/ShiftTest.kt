package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.fail
import org.junit.Test

class ShiftTest {

    @Test(expected = GameAlreadyEndedException::class)
    fun `next with game already ended should throw an exception `() {
        val shift = Shift(attempt = 1, guess = mock(), answer = mock(), maxAttempts = 1)

        shift.next(mock(), answer = mock())
    }

    @Test
    fun `next should return next shift with attempt number updated`() {
        val guess = mock<Guess>()
        val answer = mock<Answer>()
        val shift = Shift(attempt = 1, guess = mock(), answer = answer, maxAttempts = 2)

        val next = shift.next(guess, answer)

        assertThat(next).isEqualTo(Shift(attempt = 2, guess = guess, answer = answer, maxAttempts = 2))
    }

    @Test
    fun `executeIf with game won should execute won`() {
        val answer = Answer(bulls = 4, cows = 0)
        val shift = Shift(attempt = 1, guess = mock(), answer = answer, maxAttempts = 2)

        shift.executeIf(
            won = { assertThat(true) },
            over = { fail() },
            inProgress = { fail() }
        )
    }

    @Test
    fun `executeIf with game over should execute over`() {
        val answer = Answer(bulls = 3, cows = 0)
        val shift = Shift(attempt = 2, guess = mock(),answer = answer, maxAttempts = 2)

        shift.executeIf(
            won = { fail() },
            over = { assertThat(true) },
            inProgress = { fail() }
        )
    }

    @Test
    fun `executeIf with game in progress should execute in progress`() {
        val answer = Answer(bulls = 3, cows = 0)
        val shift = Shift(attempt = 1, guess = mock(), answer = answer, maxAttempts = 2)

        shift.executeIf(
            won = { fail() },
            over = { fail() },
            inProgress = { assertThat(true) }
        )
    }
}