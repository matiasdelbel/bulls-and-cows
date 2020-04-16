package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.exception.GameNoStartedException
import com.delbel.bullscows.game.domain.exception.GameNoWonException
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
    fun `throwIfIsOver with game not started should no throw exception`() {
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2)

        runCatching { game.throwIfIsOver(exception = mock()) }
            .onSuccess { assert(true) }
            .onFailure { fail() }
    }

    @Test
    fun `throwIfIsOver with game in progress should no throw exception`() {
        val shift = Shift(attempt = 1, guess = mock(), answer = Answer(1, 2), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        runCatching { game.throwIfIsOver(exception = mock()) }
            .onSuccess { assert(true) }
            .onFailure { fail() }
    }

    @Test
    fun `throwIfIsOver with game lost should throw exception`() {
        val expected = Exception()
        val shift = Shift(attempt = 2, guess = mock(), answer = Answer(1, 2), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        runCatching { game.throwIfIsOver(expected) }
            .onSuccess { fail() }
            .onFailure { assertThat(it).isEqualTo(expected) }
    }

    @Test
    fun `throwIfIsOver with game won should throw exception`() {
        val expected = Exception()
        val shift = Shift(attempt = 2, guess = mock(), answer = Answer(4, 0), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        runCatching { game.throwIfIsOver(expected) }
            .onSuccess { fail() }
            .onFailure { assertThat(it).isEqualTo(expected) }
    }

    @Test
    fun `points with game won should delegate to task to shift`() {
        val shift = Shift(attempt = 1, answer = Answer(bulls = 4, cows = 0), guess = mock(), maxAttempts = 1)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        val total = game.points()

        assertThat(total).isEqualTo(10)
    }

    @Test(expected = GameNoStartedException::class)
    fun `points with game not started should throw exception`() {
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2)

        game.points()
    }

    @Test(expected = GameNoWonException::class)
    fun `points with game lost should delegate to task to shift`() {
        val shift = Shift(attempt = 1, answer = Answer(bulls = 1, cows = 0), guess = mock(), maxAttempts = 1)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        game.points()
    }

    @Test(expected = GameNoWonException::class)
    fun `points with game in progress should delegate to task to shift`() {
        val shift = Shift(attempt = 1, answer = Answer(bulls = 1, cows = 0), guess = mock(), maxAttempts = 2)
        val game = Game(id = mock(), secret = mock(), maxAttempts = 2, current = shift)

        game.points()
    }
}