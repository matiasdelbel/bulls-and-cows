package com.delbel.bullscows.session.gateway

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.exception.GameNoWonException
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.gateway.database.SessionDao
import com.delbel.bullscows.session.gateway.model.SessionDo
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class LocalSessionRepositoryTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    @Test
    fun `create should create a new session and return it id`() = mainRule.runBlockingTest {
        val dao = mock<SessionDao> { onBlocking { insert(any()) } doReturn 123 }
        val repository = LocalSessionRepository(dao)

        val id = repository.create()

        assertThat(id).isEqualTo(SessionId(value = 123))
        argumentCaptor<SessionDo> {
            verify(dao).insert(capture())
            assertThat(firstValue).isEqualTo(SessionDo())
        }
    }

    @Test
    fun `obtainBy should return session from data base`() = mainRule.runBlockingTest {
        val expected = mock<Session>()
        val sessionDo = mock<SessionDo> { on { asModel() } doReturn expected }
        val dao = mock<SessionDao> { onBlocking { obtainBy(sessionId = 123) } doReturn sessionDo }
        val repository = LocalSessionRepository(dao)

        val session = repository.obtainBy(id = SessionId(value = 123))

        assertThat(session).isEqualTo(expected)
    }

    @Test(expected = GameNoWonException::class)
    fun `addGameWon with game no won should throw exception`() = mainRule.runBlockingTest {
        val game = Game(id = mock(), secret = mock(), maxAttempts = 1)
        val dao = mock<SessionDao>()
        val repository = LocalSessionRepository(dao)

        repository.addGameWon(id = mock(), game = game)
    }

    @Test
    fun `addGameWon should update points and guessed on data base`() = mainRule.runBlockingTest {
        val game = mock<Game> { on { points() } doReturn 10 }
        val session = mock<Session> {
            on { guessed } doReturn 1
            on { points } doReturn 10
        }
        val sessionDo = mock<SessionDo> { on { asModel() } doReturn session }
        val dao = mock<SessionDao> { onBlocking { obtainBy(sessionId = 123) } doReturn sessionDo }
        val repository = LocalSessionRepository(dao)

        repository.addGameWon(id = SessionId(value = 123), game = game)

        verify(dao).update(sessionId = 123L, guessed = 2, points = 20)
    }
}