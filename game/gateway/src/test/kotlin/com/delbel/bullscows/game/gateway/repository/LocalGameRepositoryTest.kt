package com.delbel.bullscows.game.gateway.repository

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.gateway.LocalGameRepository
import com.delbel.bullscows.game.gateway.MainCoroutineRule
import com.delbel.bullscows.game.gateway.database.GameDao
import com.delbel.bullscows.game.gateway.database.ShiftDao
import com.delbel.bullscows.game.gateway.model.GameDo
import com.delbel.bullscows.game.gateway.model.ShiftDo
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class LocalGameRepositoryTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    @Test
    fun `saveGame should create DO and added to the DB`() = mainRule.runBlockingTest {
        val secret = Secret(first = 1, second = 2, third = 3, fourth = 4)
        val gameDo = GameDo.createFrom(secret = secret, maxAttempts = 2)
        val gameDao = mock<GameDao> { onBlocking { insert(gameDo) } doReturn 3 }
        val repository = LocalGameRepository(gameDao = gameDao, shiftDao = mock())

        val gameId = repository.saveGame(secret = secret, maxAttempts = 2)

        assertThat(gameId).isEqualTo(GameId(id = 3))
    }

    @Test
    fun `obtainGameBy should obtain game from DB`() = mainRule.runBlockingTest {
        val shift = mock<Shift>()
        val expectedGame = mock<Game>()
        val shiftDo = mock<ShiftDo> { on { asModel() } doReturn shift }
        val gameDo = mock<GameDo> { on { asModel(currentShift = shift) } doReturn expectedGame }
        val gameDao = mock<GameDao> { onBlocking { obtainBy(gameId = 12) } doReturn gameDo }
        val shiftDao = mock<ShiftDao> { onBlocking { obtainLastFor(gameId = 12) } doReturn shiftDo }
        val repository = LocalGameRepository(gameDao = gameDao, shiftDao = shiftDao)

        val game = repository.obtainGameBy(id = GameId(12))

        assertThat(game).isEqualTo(expectedGame)
    }

    @Test
    fun `addShift should save shift on the DB`() = mainRule.runBlockingTest {
        val guess = Guess(first = 1, second = 2, third = 3, fourth = 4)
        val answer = Answer(bulls = 2, cows = 1)
        val shift = Shift(attempt = 1, guess = guess, answer = answer, maxAttempts = 3)
        val shiftDo = ShiftDo.createFrom(id = GameId(id = 12), shift = shift)
        val shiftDao = mock<ShiftDao>()
        val repository = LocalGameRepository(gameDao = mock(), shiftDao = shiftDao)

        repository.addShift(gameId = GameId(12), shift = shift)

        verify(shiftDao).insert(shiftDo)
    }

    @Test
    fun `shiftsFor should return a flow of shifts`() = mainRule.runBlockingTest {
        val expectedShift = mock<Shift>()
        val shiftDo = mock<ShiftDo> { on { asModel() } doReturn expectedShift }
        val shiftsFlow = flow { emit(listOf(shiftDo)) }
        val shiftDao = mock<ShiftDao> { on { obtainFor(gameId = 12) } doReturn shiftsFlow }
        val repository = LocalGameRepository(gameDao = mock(), shiftDao = shiftDao)

        val shifts = repository.shiftsFor(id = GameId(12)).single()

        assertThat(shifts).isEqualTo(listOf(expectedShift))
    }
}