package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.session.domain.SessionId
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import java.lang.Long.MIN_VALUE

class LocalSessionIdRepositoryTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    @Test
    fun `obtainCurrentOrCreate should return current session id`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn 123
        }
        val repository = LocalSessionIdRepository(preferences)

        val sessionId = repository.obtainCurrentOrCreate { throw Exception() }

        assertThat(sessionId).isEqualTo(SessionId(value = 123))
    }

    @Test
    fun `id without current should obtain it from creator`() = mainRule.runBlockingTest {
        val preferencesEditor = mock<SharedPreferences.Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", MIN_VALUE) } doReturn MIN_VALUE doReturn 123
            on { edit() } doReturn preferencesEditor
        }
        val sessionIdFromCreator = SessionId(value = 123)
        val repository = LocalSessionIdRepository(preferences)

        val sessionId = repository.obtainCurrentOrCreate { sessionIdFromCreator }

        assertThat(sessionId).isEqualTo(sessionIdFromCreator)
        verify(preferencesEditor).putLong("CURRENT_SESSION_ID", 123)
        verify(preferencesEditor).apply()
    }

    @Test
    fun `update should save the new id on preferences`() {
        val preferencesEditor = mock<SharedPreferences.Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> { on { edit() } doReturn preferencesEditor }
        val repository = LocalSessionIdRepository(preferences)

        repository.removeCurrent()

        verify(preferencesEditor).putLong("CURRENT_SESSION_ID", MIN_VALUE)
        verify(preferencesEditor).apply()
    }
}