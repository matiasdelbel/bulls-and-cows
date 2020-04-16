package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId

interface CurrentSessionRepository {

    fun register(sessionId: SessionId, gameId: GameId)

    fun updateGameId(gameId: GameId)

    fun obtainSessionId(): SessionId

    fun obtainGameId(): GameId

    fun clear()
}

class NoSessionException : RuntimeException()

class NoGameException : RuntimeException()