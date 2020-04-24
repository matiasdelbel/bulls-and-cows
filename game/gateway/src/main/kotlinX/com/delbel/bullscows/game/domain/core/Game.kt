package com.delbel.bullscows.game.domain.core

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.gateway.model.GameDo

internal fun Game.toDo() = GameDo(maxAttempts = maxAttempts, secret = secret.toDo())