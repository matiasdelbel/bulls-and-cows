package com.delbel.bullscows.session.presentation.best

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.repository.SessionRepository
import com.delbel.bullscows.session.presentation.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class BestScoresViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `listing should obtain flow from data base`() = coroutineRule.runBlockingTest {
        val session = mock<Session>()
        val sessionsFlow = flow { emit(listOf(session)) }
        val repository = mock<SessionRepository> { on { obtainAll() } doReturn sessionsFlow }
        val viewModel = BestScoresViewModel(repository)
        val observer = mock<Observer<List<Session>>>()

        viewModel.listing.observeForever(observer)

        argumentCaptor<List<Session>> {
            verify(observer).onChanged(capture())
            assertThat(firstValue).containsExactly(session)
        }
    }
}