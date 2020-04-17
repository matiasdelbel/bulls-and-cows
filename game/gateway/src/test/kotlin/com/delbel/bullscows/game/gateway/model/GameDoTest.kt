package com.delbel.bullscows.game.gateway.model

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.gateway.model.GameDo.SecretDo
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class GameDoTest {

    @Test
    fun `createFrom should create a new instance of the DO`() {
        val game = Game(
            secret = Secret(first = 1, second = 2, third = 3, fourth = 4),
            maxAttempts = 7
        )

        val gameDo = GameDo.createFrom(game)

        assertThat(gameDo.id).isEqualTo(0)
        assertThat(gameDo.secret).isEqualTo(SecretDo(first = 1, second = 2, third = 3, fourth = 4))
        assertThat(gameDo.maxAttempts).isEqualTo(7)
    }

    @Test
    fun `asModel should create a new instance of the business object`() {
        val shift = mock<Shift>()
        val gameDo = GameDo(maxAttempts = 3, secret = SecretDo(first = 1, second = 2, third = 3, fourth = 4))
            .also { it.id = 123 }
        val expectedGame = Game(
            id = GameId(id = 123),
            secret = Secret(first = 1, second = 2, third = 3, fourth = 4),
            maxAttempts = 3,
            current = shift
        )

        val game = gameDo.asModel(currentShift = shift)

        assertThat(game).isEqualTo(expectedGame)
    }
}