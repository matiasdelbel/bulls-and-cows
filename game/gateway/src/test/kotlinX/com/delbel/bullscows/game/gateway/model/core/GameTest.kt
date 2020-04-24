package com.delbel.bullscows.game.gateway.model.core

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.core.toDo
import com.delbel.bullscows.game.domain.toDo
import com.delbel.bullscows.game.gateway.model.GameDo
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GameTest {

    @Test
    fun `toDo should create a new instance of the DO`() {
        val game = Game(secret = Secret(first = 1, second = 2, third = 3, fourth = 4), maxAttempts = 7)

        val gameDo = game.toDo()

        assertThat(gameDo.id).isEqualTo(0)
        assertThat(gameDo.secret).isEqualTo(GameDo.SecretDo(first = 1, second = 2, third = 3, fourth = 4))
        assertThat(gameDo.maxAttempts).isEqualTo(7)
    }
}