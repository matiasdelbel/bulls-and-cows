package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId
import kotlinx.coroutines.flow.Flow

interface CurrentSessionRepository {

    fun register(sessionId: SessionId, gameId: GameId)

    fun updateGameId(gameId: GameId)

    fun obtainSessionId(): Flow<SessionId?>

    fun obtainGameId(): Flow<GameId?>

    fun clear()
}