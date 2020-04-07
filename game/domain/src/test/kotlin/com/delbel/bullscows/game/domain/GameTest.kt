package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.fail
import org.junit.Test

class GameTest {

    @Test
    fun `guess should return executed shift`() {
        val answer = mock<Answer>()
        val guess = mock<Guess>()
        val secret = mock<Secret> { on { guess(guess) } doReturn answer }
        val game = Game(id = mock(), secret = secret, maxAttempts = 2)

        val shift = game.guess(guess)

        val expectedShift = Shift(attempt = 1, guess = guess, answer = answer, maxAttempts = 2)
        assertThat(shift).isEqualTo(expectedShift)
    }

    @Test
    fun `executeIf with null shift should execute isAboutToStart`() {
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = null)

        game.executeIf(
            isAboutToStart = { assert(true) },
            isInProgress = { fail() },
            hasWon = { fail() },
            hasLost = { fail() })
    }

    @Test
    fun `executeIf with running shift should execute isInProgress`() {
        val shift = Shift(attempt = 1, guess = mock(), answer = mock(), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        game.executeIf(
            isAboutToStart = { fail() },
            isInProgress = { assert(true) },
            hasWon = { fail() },
            hasLost = { fail() })
    }

    @Test
    fun `executeIf with game won should execute hasWon`() {
        val shift = Shift(attempt = 1, guess = mock(), answer = Answer(4, 0), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        game.executeIf(
            isAboutToStart = { fail() },
            isInProgress = { fail() },
            hasWon = { assert(true) },
            hasLost = { fail() })
    }

    @Test
    fun `executeIf with game lost should execute hasLost`() {
        val shift = Shift(attempt = 2, guess = mock(), answer = Answer(0, 0), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        game.executeIf(
            isAboutToStart = { fail() },
            isInProgress = { fail() },
            hasWon = { fail() },
            hasLost = { assert(true) })
    }
}