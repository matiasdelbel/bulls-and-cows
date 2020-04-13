package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.session.domain.SessionId
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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
}