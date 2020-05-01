package com.delbel.bullscows.session.gateway

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.gateway.database.SessionDao
import com.delbel.bullscows.session.gateway.model.SessionDo
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
        val dao = mock<SessionDao> {
            onBlocking { obtainBy(sessionId = 123) } doReturn flow { emit(sessionDo) }
        }
        val repository = LocalSessionRepository(dao)

        val session = repository.obtainBy(id = SessionId(value = 123))

        assertThat(session.first()).isEqualTo(expected)
    }

    @Test
    fun `obtainAll should return sessions from data base`() = mainRule.runBlockingTest {
        val expected = mock<Session>()
        val sessionDo = mock<SessionDo> { on { asModel() } doReturn expected }
        val dao = mock<SessionDao> {
            onBlocking { obtainAll() } doReturn flow { emit(listOf(sessionDo)) }
        }
        val repository = LocalSessionRepository(dao)

        val session = repository.obtainAll()

        assertThat(session.first()).containsExactly(expected)
    }

    @Test
    fun `addGameWon should update points and guessed on data base`() = mainRule.runBlockingTest {
        val game = mock<Game>()
        val updatedSession = Session(id = SessionId(value = 123), guessed = 1, points = 20)
        val session = mock<Session> { on { plus(game) } doReturn updatedSession }
        val sessionDo = mock<SessionDo> { on { asModel() } doReturn session }
        val dao = mock<SessionDao> {
            onBlocking { obtainBy(sessionId = 123) } doReturn flow { emit(sessionDo) }
        }
        val repository = LocalSessionRepository(dao)

        repository.addGameWon(id = SessionId(value = 123), game = game)

        verify(dao).update(session = SessionDo(guessed = 1, points = 20))
    }
}