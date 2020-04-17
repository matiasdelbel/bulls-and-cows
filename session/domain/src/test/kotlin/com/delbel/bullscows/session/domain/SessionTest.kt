package com.delbel.bullscows.session.domain

import com.delbel.bullscows.game.domain.Game
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class SessionTest {

    @Test
    fun `add game should create an updated session with the result`() {
        val sessionId = mock<SessionId>()
        val session = Session(id = sessionId, guessed = 1, points = 20)
        val game = mock<Game> { on { points() } doReturn 15 }

        val updatedSession = session + game

        assertThat(updatedSession).isEqualTo(Session(id = sessionId, guessed = 2, points = 35))
    }
}