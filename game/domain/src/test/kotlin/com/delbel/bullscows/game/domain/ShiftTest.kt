package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.exception.GameEndedException
import com.delbel.bullscows.game.domain.exception.GameNoEndedException
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.fail
import org.junit.Test

class ShiftTest {

    @Test(expected = GameEndedException::class)
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

        assertThat(next).isEqualTo(
            Shift(
                attempt = 2,
                guess = guess,
                answer = answer,
                maxAttempts = 2
            )
        )
    }

    @Test
    fun `executeIf with game won should execute won`() {
        val answer = Answer(bulls = 4, cows = 0)
        val shift = Shift(attempt = 1, guess = mock(), answer = answer, maxAttempts = 2)

        shift.executeIf(
            won = { assertThat(true) },
            lost = { fail() },
            inProgress = { fail() }
        )
    }

    @Test
    fun `executeIf with game over should execute over`() {
        val answer = Answer(bulls = 3, cows = 0)
        val shift = Shift(attempt = 2, guess = mock(), answer = answer, maxAttempts = 2)

        shift.executeIf(
            won = { fail() },
            lost = { assertThat(true) },
            inProgress = { fail() }
        )
    }

    @Test
    fun `executeIf with game in progress should execute in progress`() {
        val answer = Answer(bulls = 3, cows = 0)
        val shift = Shift(attempt = 1, guess = mock(), answer = answer, maxAttempts = 2)

        shift.executeIf(
            won = { fail() },
            lost = { fail() },
            inProgress = { assertThat(true) }
        )
    }

    @Test(expected = GameNoEndedException::class)
    fun `points with game in progress should throw exception`() {
        val answer = Answer(bulls = 1, cows = 1)
        val shift = Shift(attempt = 1, guess = mock(), answer = answer, maxAttempts = 2)

        shift.points()
    }

    @Test(expected = GameEndedException::class)
    fun `points with game lost should throw exception`() {
        val answer = Answer(bulls = 1, cows = 1)
        val shift = Shift(attempt = 2, guess = mock(), answer = answer, maxAttempts = 2)

        shift.points()
    }

    @Test
    fun `points should return point 10 multiply max attempts divide attempt`() {
        val answer = Answer(bulls = 4, cows = 0)
        val shift = Shift(attempt = 1, guess = mock(), answer = answer, maxAttempts = 2)

        val points = shift.points()

        assertThat(points).isEqualTo(20)
    }
}