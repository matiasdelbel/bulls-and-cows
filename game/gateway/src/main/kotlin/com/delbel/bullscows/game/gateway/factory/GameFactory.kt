package com.delbel.bullscows.game.gateway.factory

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.core.Secret
import javax.inject.Inject

class GameFactory @Inject constructor() {

    companion object {
        private val SECRET_POSSIBLE_VALUES = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        private const val MAX_ATTEMPTS = 7
    }

    fun create() = Game(secret = createSecret(), maxAttempts = MAX_ATTEMPTS)

    private fun createSecret(): Secret {
        val secretValues = SECRET_POSSIBLE_VALUES.shuffled().take(n = 4).shuffled()

        return Secret(
            first = secretValues[0],
            second = secretValues[1],
            third = secretValues[2],
            fourth = secretValues[3]
        )
    }
}