package com.delbel.bullscows.game.gateway.factory

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.core.Secret
import javax.inject.Inject

internal class GameFactory @Inject constructor() : Game.Factory {

    companion object {
        private val SECRET_SEEDS = (0..9)
        private const val MAX_ATTEMPTS = 7
    }

    override fun create() = Game(secret = secret(), maxAttempts = MAX_ATTEMPTS)

    private fun secret(): Secret {
        val secret = SECRET_SEEDS.shuffled().take(4)

        return Secret(first = secret[0], second = secret[1], third = secret[2], fourth = secret[3])
    }
}