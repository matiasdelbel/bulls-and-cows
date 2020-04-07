package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun create(initialGame: Game): Session

    suspend fun obtainLastSessionOrDefault(default: suspend () -> Session): Session

    suspend fun obtainSessionBy(id: SessionId): Session

    suspend fun addGame(id: SessionId, game: Game)

    fun gamesFor(id: SessionId): Flow<List<Game>>
}


