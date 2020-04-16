package com.delbel.bullscows.session.gateway

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.exception.GameNoWonException
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.gateway.database.SessionDao
import com.delbel.bullscows.session.gateway.model.SessionDo
import javax.inject.Inject

internal class LocalSessionRepository @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun create(): SessionId {
        val id = sessionDao.insert(session = SessionDo())

        return SessionId(value = id)
    }

    override suspend fun obtainBy(id: SessionId): Session {
        val sessionDo = sessionDao.obtainBy(sessionId = id.value)

        return sessionDo.asModel()
    }

    override suspend fun addGameWon(id: SessionId, game: Game) {
        val session = obtainBy(id = id)
        val points = game.points()

        sessionDao.update(
            sessionId = id.value,
            guessed = session.guessed + 1,
            points = session.points + points
        )
    }
}