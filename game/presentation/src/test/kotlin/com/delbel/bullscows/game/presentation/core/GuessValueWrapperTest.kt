package com.delbel.bullscows.game.presentation.core

import android.widget.Button
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class GuessValueWrapperTest {

    @Test
    fun `wrapNext should set next value to the button`() {
        val button = mock<Button> { on { text } doReturn "1" }
        val wrapper = GuessValueWrapper()

        wrapper.wrapNext(button)

        verify(button).text = "2"
    }

    @Test
    fun `wrapNext with nine should set zero as next value to the button`() {
        val button = mock<Button> { on { text } doReturn "9" }
        val wrapper = GuessValueWrapper()

        wrapper.wrapNext(button)

        verify(button).text = "0"
    }
}