package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class ShiftTest {

    @Test(expected = IllegalStateException::class)
    fun `next with game already ended should throw an exception `() {
        val shift = Shift(attempt = 1, answer = mock(), maxAttempts = 1)

        shift.next(answer = mock())
    }

    @Test
    fun `next should return next shift with attempt number updated`() {
        val answer = mock<Answer>()
        val shift = Shift(attempt = 1, answer = answer, maxAttempts = 2)

        val next = shift.next(answer)

        assertThat(next).isEqualTo(Shift(attempt = 2, answer = answer, maxAttempts = 2))
    }
}