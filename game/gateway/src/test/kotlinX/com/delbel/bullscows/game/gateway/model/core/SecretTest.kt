package com.delbel.bullscows.game.gateway.model.core

import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.core.toDo
import com.delbel.bullscows.game.gateway.model.GameDo
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SecretTest {

    @Test
    fun `toDo should create a new instance of the DO`() {
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)

        val secretDo = secret.toDo()

        assertThat(secretDo).isEqualTo(GameDo.SecretDo(first = 1, second = 2, third = 3, fourth = 4))
    }
}