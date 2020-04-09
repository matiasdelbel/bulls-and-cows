package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.exception.GameNoStartedException
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.fail
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
    fun `executeIfHasNotWon with game not started should execute block`() {
        val expected = Exception()
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2)

        runCatching { game.executeIfHasNotWon { throw expected } }
            .onSuccess { fail() }
            .onFailure { assertThat(it).isEqualTo(expected) }
    }

    @Test
    fun `executeIfHasNotWon with game in progress should execute block`() {
        val expected = Exception()
        val shift = Shift(attempt = 1, guess = mock(), answer = Answer(1, 2), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        runCatching { game.executeIfHasNotWon { throw expected } }
            .onSuccess { fail() }
            .onFailure { assertThat(it).isEqualTo(expected) }
    }

    @Test
    fun `executeIfHasNotWon with game lost should execute block`() {
        val expected = Exception()
        val shift = Shift(attempt = 2, guess = mock(), answer = Answer(1, 2), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        runCatching { game.executeIfHasNotWon { throw expected } }
            .onSuccess { fail() }
            .onFailure { assertThat(it).isEqualTo(expected) }
    }

    @Test
    fun `executeIfHasNotWon with game won should execute block`() {
        val shift = Shift(attempt = 2, guess = mock(), answer = Answer(4, 0), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        runCatching { game.executeIfHasNotWon { throw Exception() } }
            .onSuccess { assert(true) }
            .onFailure { fail() }
    }

    @Test
    fun `points should delegate to task to shift`() {
        val shift = mock<Shift> { on { points() } doReturn 10 }
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        val total = game.points()

        assertThat(total).isEqualTo(10)
    }

    @Test(expected = GameNoStartedException::class)
    fun `points with game not started should throw exception`() {
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2)

        game.points()
    }
}