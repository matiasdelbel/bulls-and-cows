package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId

interface CurrentSessionRepository {

    fun register(sessionId: SessionId, gameId: GameId)

    fun updateGameId(gameId: GameId)

    suspend fun obtainSessionIdOrCreate(creator: suspend () -> SessionId): SessionId

    suspend fun obtainSessionIdOrThrow(exception: Exception): SessionId

    fun removeSessionId()
}