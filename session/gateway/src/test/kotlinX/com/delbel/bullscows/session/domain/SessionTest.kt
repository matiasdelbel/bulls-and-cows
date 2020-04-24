package com.delbel.bullscows.session.domain

import com.delbel.bullscows.session.gateway.model.SessionDo
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SessionTest {

    @Test
    fun `toDo should create a new instance of the DO`() {
        val expected = SessionDo(guessed = 12, points = 100).also { it.id = 1 }
        val session = Session(id = SessionId(value = 1), guessed = 12, points = 100)

        val result = session.toDo()

        assertThat(result).isEqualTo(expected)
    }
}