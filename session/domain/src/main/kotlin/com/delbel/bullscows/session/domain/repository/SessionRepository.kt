package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId

interface SessionRepository {

    suspend fun create(): SessionId

    suspend fun obtainBy(id: SessionId): Session

    suspend fun addGameWon(id: SessionId, game: Game)
}