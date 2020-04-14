package com.delbel.bullscows.game.domain.core

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class SecretTest {

    @Test
    fun `create with different four-digit secret should create it`() {
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)

        assertThat(secret).isEqualTo(Secret(first = 1, second = 2, third = 3, fourth = 4))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with repeated four-digit secret should throw and exception`() {
        Secret(first = 1, second = 2, third = 2, fourth = 4)
    }

    @Test
    fun `guess should delegate task to guess object`() {
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)
        val guess = mock<Guess>()

        secret.guess(guess)

        verify(guess).guess(first = 1, second = 2, third = 3, fourth = 4)
    }

    @Test
    fun `asString should return the secret values as string`() {
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)

        val result = secret.asString()

        assertThat(result).isEqualTo("1 2 3 4")
    }
}