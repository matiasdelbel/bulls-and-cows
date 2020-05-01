package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun create(): SessionId

    fun obtainBy(id: SessionId): Flow<Session>

    fun obtainAll(): Flow<List<Session>>

    suspend fun addGameWon(id: SessionId, game: Game)
}