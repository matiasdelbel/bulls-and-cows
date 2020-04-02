package com.delbel.bullscows.game.domain.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GuessTest {

    @Test
    fun `create with different four-digit guess should create it`() {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)

        assertThat(guess).isEqualTo(Guess(first = 1, second = 2, third = 3, fourth = 4))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with repeated four-digit guess should throw and exception`() {
        Guess(first = 1, second = 2, third = 2, fourth = 4)
    }

    @Test
    fun `guess with 1234 and secret 4271 should return 1 bull and 2 cows`() {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)

        val answer = guess.guess(first = 4, second = 2, third = 7, fourth = 1)

        assertThat(answer).isEqualTo(Answer(bulls = 1, cows = 2))
    }

    @Test
    fun `guess with 1234 and secret 1234 should return 4 bull and 0 cows`() {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)

        val answer = guess.guess(first = 1, second = 2, third = 3, fourth = 4)

        assertThat(answer).isEqualTo(Answer(bulls = 4, cows = 0))
    }

    @Test
    fun `guess with 1234 and secret 4321 should return 0 bull and 4 cows`() {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)

        val answer = guess.guess(first = 4, second = 3, third = 2, fourth = 1)

        assertThat(answer).isEqualTo(Answer(bulls = 0, cows = 4))
    }

    @Test
    fun `guess with 1234 and secret 5678 should return 0 bull and 0 cows`() {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)

        val answer = guess.guess(first = 5, second = 6, third = 7, fourth = 8)

        assertThat(answer).isEqualTo(Answer(bulls = 0, cows = 0))
    }
}