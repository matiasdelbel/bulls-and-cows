package com.delbel.bullscows.game.gateway.factory

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.core.Secret
import javax.inject.Inject

class GameFactory @Inject constructor() {

    fun create(): Game {
        // TODO implement me
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)

        return Game(secret = secret, maxAttempts = 7)
    }
}