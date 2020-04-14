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

class LocalCurrentSessionTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    @Test
    fun `id should obtain session id saved on preferences`() = mainRule.runBlockingTest {
        val preferences = mock<SharedPreferences> {
            on { getLong("CURRENT_SESSION_ID", Long.MAX_VALUE) } doReturn 123
        }
        val currentSession = LocalCurrentSession(preferences)

        val sessionId = currentSession.id()

        assertThat(sessionId).isEqualTo(SessionId(value = 123))
    }

    @Test
    fun `update should save the new id on preferences`() {
        val preferencesEditor = mock<SharedPreferences.Editor> {
            on { putLong(any(), any()) } doReturn mock
        }
        val preferences = mock<SharedPreferences> { on { edit() } doReturn preferencesEditor }
        val currentSession = LocalCurrentSession(preferences)

        currentSession.update(id = SessionId(value = 123))

        verify(preferencesEditor).putLong("CURRENT_SESSION_ID", 123)
        verify(preferencesEditor).apply()
    }
}