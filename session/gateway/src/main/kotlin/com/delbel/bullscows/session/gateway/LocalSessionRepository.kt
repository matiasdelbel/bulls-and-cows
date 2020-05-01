package com.delbel.bullscows.session.gateway

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.domain.toDo
import com.delbel.bullscows.session.gateway.database.SessionDao
import com.delbel.bullscows.session.gateway.model.SessionDo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalSessionRepository @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun create(): SessionId =
        SessionId(value = sessionDao.insert(session = SessionDo()))

    override fun obtainBy(id: SessionId) =
        sessionDao.obtainBy(sessionId = id.value).map { it.asModel() }

    override fun obtainAll(): Flow<List<Session>> =
        sessionDao.obtainAll().map { sessions -> sessions.map { it.asModel() } }

    override suspend fun addGameWon(id: SessionId, game: Game) {
        val updatedSession = obtainBy(id = id).first() + game
        sessionDao.update(session = updatedSession.toDo())
    }
}