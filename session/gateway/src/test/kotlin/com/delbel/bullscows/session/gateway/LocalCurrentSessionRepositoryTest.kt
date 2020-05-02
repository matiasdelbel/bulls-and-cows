package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import android.content.SharedPreferences.*
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.session.domain.SessionId
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import java.lang.Long.MIN_VALUE

class LocalCurrentSessionRepositoryTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    @Test
    fun `register should update current session id and game id`() = mainRule.runBlockingTest {
        val preferencesEditor = mock<Editor> {
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
        val preferencesEditor = mock<Editor> {
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
    fun `obtainSessionId without session id should throw`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn MIN_VALUE
        }
        val repository = LocalCurrentSessionRepository(preferences)
        mockPreferencesChange(preferences, key = "CURRENT_SESSION_ID")

        val sessionId = repository.obtainSessionId().first()

        assertThat(sessionId).isNull()
    }

    @Test
    fun `obtainSessionId with session id should return it`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalCurrentSessionRepository(preferences)
        mockPreferencesChange(preferences, key = "CURRENT_SESSION_ID")

        val sessionId = repository.obtainSessionId().first()

        assertThat(sessionId).isEqualTo(SessionId(value = 123))
    }

    @Test
    fun `obtainGameId without game id should throw`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_GAME_ID", MIN_VALUE) } doReturn MIN_VALUE
        }
        val repository = LocalCurrentSessionRepository(preferences)
        mockPreferencesChange(preferences, key = "CURRENT_GAME_ID")

        val gameId = repository.obtainGameId().first()

        assertThat(gameId).isNull()
    }

    @Test
    fun `obtainGameId with game id should return it`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_GAME_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalCurrentSessionRepository(preferences)
        mockPreferencesChange(preferences, key = "CURRENT_GAME_ID")

        val gameId = repository.obtainGameId().first()

        assertThat(gameId).isEqualTo(GameId(id = 123))
    }

    @Test
    fun `clear should clear current session and game id`() = mainRule.runBlockingTest {
        val preferencesEditor = mock<Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> { on { edit() } doReturn preferencesEditor }
        val repository = LocalCurrentSessionRepository(preferences)

        repository.clear()

        verify(preferencesEditor).putLong("CURRENT_SESSION_ID", MIN_VALUE)
        verify(preferencesEditor).putLong("CURRENT_GAME_ID", MIN_VALUE)
        verify(preferencesEditor, times(2)).apply()
    }

    private fun mockPreferencesChange(preferences: SharedPreferences, key: String) = argumentCaptor<OnSharedPreferenceChangeListener>{
        verify(preferences).registerOnSharedPreferenceChangeListener(capture())
        firstValue.onSharedPreferenceChanged(preferences, key)
    }
}