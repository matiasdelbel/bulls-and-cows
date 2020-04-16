package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import java.lang.Long.MIN_VALUE

class LocalCurrentSessionRepositoryTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    @Test
    fun `register should update current session id and game id`() = mainRule.runBlockingTest {
        val preferencesEditor = mock<SharedPreferences.Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> { on { edit() } doReturn preferencesEditor }
        val repository = LocalCurrentSessionRepository(preferences)
        val sessionId = SessionId(value = 123)
        val gameId = GameId(id = 345)

        repository.register(sessionId, gameId)

        verify(preferencesEditor).putLong("CURRENT_SESSION_ID", 123)
        verify(preferencesEditor).putLong("CURRENT_GAME_ID", 345)
        verify(preferencesEditor, times(2)).apply()
    }

    @Test
    fun `updateGameId without should update current game id`() = mainRule.runBlockingTest {
        val preferencesEditor = mock<SharedPreferences.Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> { on { edit() } doReturn preferencesEditor }
        val gameId = GameId(id = 123)
        val repository = LocalCurrentSessionRepository(preferences)

        repository.updateGameId(gameId)

        verify(preferencesEditor).putLong("CURRENT_GAME_ID", 123)
        verify(preferencesEditor).apply()
    }

    @Test
    fun `obtainSessionIdOrCreate should return current session id`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalCurrentSessionRepository(preferences)

        val sessionId = repository.obtainSessionIdOrCreate { throw Exception() }

        assertThat(sessionId).isEqualTo(SessionId(value = 123))
    }

    @Test
    fun `obtainSessionIdOrCreate without current should obtain it from creator`() = mainRule.runBlockingTest {
        val preferencesEditor = mock<SharedPreferences.Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn MIN_VALUE doReturn 123
            on { edit() } doReturn preferencesEditor
        }
        val sessionIdFromCreator = SessionId(value = 123)
        val repository = LocalCurrentSessionRepository(preferences)

        val sessionId = repository.obtainSessionIdOrCreate { sessionIdFromCreator }

        assertThat(sessionId).isEqualTo(sessionIdFromCreator)
        verify(preferencesEditor).putLong("CURRENT_SESSION_ID", 123)
        verify(preferencesEditor).apply()
    }

    @Test(expected = RuntimeException::class)
    fun `obtainSessionIdOrThrow without session id should throw`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn MIN_VALUE
        }
        val repository = LocalCurrentSessionRepository(preferences)

        repository.obtainSessionIdOrThrow(exception = RuntimeException())
    }

    @Test
    fun `obtainSessionIdOrThrow with session id should return it`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalCurrentSessionRepository(preferences)

        val sessionId = repository.obtainSessionIdOrThrow(exception = RuntimeException())

        assertThat(sessionId).isEqualTo(SessionId(value = 123))
    }

    @Test(expected = RuntimeException::class)
    fun `obtainGameIdOrThrow without session id should throw`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_GAME_ID", MIN_VALUE) } doReturn MIN_VALUE
        }
        val repository = LocalCurrentSessionRepository(preferences)

        repository.obtainGameIdOrThrow(exception = RuntimeException())
    }

    @Test
    fun `obtainGameIdOrThrow with session id should return it`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_GAME_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalCurrentSessionRepository(preferences)

        val gameId = repository.obtainGameIdOrThrow(exception = RuntimeException())

        assertThat(gameId).isEqualTo(GameId(id = 123))
    }

    @Test
    fun `clear should clear current session and game id`() {
        val preferencesEditor = mock<SharedPreferences.Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> { on { edit() } doReturn preferencesEditor }
        val repository = LocalCurrentSessionRepository(preferences)

        repository.clear()

        verify(preferencesEditor).putLong("CURRENT_SESSION_ID", MIN_VALUE)
        verify(preferencesEditor).putLong("CURRENT_GAME_ID", MIN_VALUE)
        verify(preferencesEditor, times(2)).apply()
    }
}