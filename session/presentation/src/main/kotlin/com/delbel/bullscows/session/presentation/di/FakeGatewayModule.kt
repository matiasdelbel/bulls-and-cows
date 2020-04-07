package com.delbel.bullscows.session.presentation.di

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// TODO remove this
@Module
class FakeGatewayModule {

    @Provides
    fun provideSessionRepository() = object : SessionRepository {

        private val sessions = mutableMapOf<SessionId, Session>()
        private lateinit var lastSession: Session

        override suspend fun create(initialGame: Game): Session {
            val session = Session(current = initialGame)

            sessions[session.id] = session
            lastSession = session

            return session
        }

        override suspend fun obtainSessionBy(id: SessionId): Session {
            return sessions[id]!!
        }

        override suspend fun obtainLastSessionOrDefault(default: suspend () -> Session): Session {
            return if (!::lastSession.isInitialized) default()
            else lastSession
        }

        override suspend fun addGame(id: SessionId, game: Game) {
            // do nothing
        }

        override fun gamesFor(id: SessionId): Flow<List<Game>> = flow { }
    }
}