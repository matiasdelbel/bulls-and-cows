package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.NoGameException
import com.delbel.bullscows.session.domain.repository.NoSessionException
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

    @Test(expected = NoSessionException::class)
    fun `obtainSessionId without session id should throw`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn MIN_VALUE
        }
        val repository = LocalCurrentSessionRepository(preferences)

        repository.obtainSessionId()
    }

    @Test
    fun `obtainSessionId with session id should return it`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalCurrentSessionRepository(preferences)

        val sessionId = repository.obtainSessionId()

        assertThat(sessionId).isEqualTo(SessionId(value = 123))
    }

    @Test(expected = NoGameException::class)
    fun `obtainGameId without game id should throw`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_GAME_ID", MIN_VALUE) } doReturn MIN_VALUE
        }
        val repository = LocalCurrentSessionRepository(preferences)

        repository.obtainGameId()
    }

    @Test
    fun `obtainGameId with game id should return it`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_GAME_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalCurrentSessionRepository(preferences)

        val gameId = repository.obtainGameId()

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