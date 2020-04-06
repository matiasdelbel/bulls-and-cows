package com.delbel.bullscows.game.gateway.model

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Secret
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class GameDoTest {

    @Test
    fun `createFrom should create a new instance of the DO`() {
        val secretDo = GameDo.SecretDo(first = 1, second = 2, third = 3, fourth = 4)
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)

        val gameDo = GameDo.createFrom(secret = secret, maxAttempts = 3)

        assertThat(gameDo.id).isEqualTo(Long.MAX_VALUE)
        assertThat(gameDo.secret).isEqualTo(secretDo)
        assertThat(gameDo.maxAttempts).isEqualTo(3)
    }

    @Test
    fun `asModel should create a new instance of the business object`() {
        val shift = mock<Shift>()
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)
        val gameDo = GameDo.createFrom(secret = secret, maxAttempts = 3)
        val expectedGame = Game(
            id = GameId(id = Long.MAX_VALUE),
            secret = secret,
            maxAttempts = 3,
            current = shift
        )

        val game = gameDo.asModel(currentShift = shift)

        assertThat(game).isEqualTo(expectedGame)
    }
}