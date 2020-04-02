package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
}