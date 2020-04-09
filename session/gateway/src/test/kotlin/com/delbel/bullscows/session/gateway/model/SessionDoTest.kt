package com.delbel.bullscows.session.gateway.model

import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SessionDoTest {

    @Test
    fun `asModel should create a new instance of the business object`() {
        val sessionDo = SessionDo(guessed = 12, points = 100).also { it.id = 1 }
        val expected = Session(id = SessionId(value = 1), guessed = 12, points = 100)

        val session = sessionDo.asModel()

        assertThat(session).isEqualTo(expected)
    }
}