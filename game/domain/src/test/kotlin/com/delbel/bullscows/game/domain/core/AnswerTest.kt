package com.delbel.bullscows.game.domain.core

import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.fail
import org.junit.Test

class AnswerTest {

    @Test
    fun `create with valid answer should create it`() {
        val answer = Answer(bulls = 2, cows = 2)

        assertThat(answer).isEqualTo(Answer(bulls = 2, cows = 2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with negative bulls should throw an exception`() {
        Answer(bulls = -1, cows = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with bulls higher than four should throw an exception`() {
        Answer(bulls = 5, cows = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with negative cows should throw an exception`() {
        Answer(bulls = 0, cows = -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with cows higher than four should throw an exception`() {
        Answer(bulls = 0, cows = 5)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with sum between cows and bulls higher than four should throw an exception`() {
        Answer(bulls = 3, cows = 3)
    }

    @Test
    fun `executeIf with guess should execute guess`() {
        val answer = Answer(bulls = 4, cows = 0)

        answer.executeIf(guess = { assert(true) }, wrongGuess = { fail() })
    }

    @Test
    fun `executeIf with wrong guess should execute wrong guess`() {
        val answer = Answer(bulls = 1, cows = 2)

        answer.executeIf(guess = { fail() }, wrongGuess = { assert(true) })
    }
}