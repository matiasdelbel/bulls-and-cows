package com.delbel.bullscows.game.presentation.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.repository.GameRepository
import dagger.Module
import dagger.Provides

// TODO remove when repository implementation is ready
@Module
class FakeModule {

    @Provides
    fun provideGameRepository() = object : GameRepository {

        override suspend fun game(id: GameId) =
            Game(id, secret = Secret(first = 1, second = 2, third = 3, fourth = 4), maxAttempts = 2)


        override fun shifts(id: GameId): LiveData<List<Shift>> {
            val shifts = listOf(
                Shift(1, Guess(1, 2, 3, 4), Answer(1, 1), 2),
                Shift(2, Guess(4, 1, 8, 9), Answer(0, 1), 2),
                Shift(3, Guess(0, 3, 5, 6), Answer(0, 3), 2),
                Shift(4, Guess(5, 2, 7, 6), Answer(2, 0), 2),
                Shift(5, Guess(6, 1, 9, 4), Answer(0, 0), 2),
                Shift(6, Guess(1, 4, 0, 8), Answer(2, 1), 2)
            )

            return MutableLiveData<List<Shift>>().also { it.value = shifts }
        }

        override suspend fun update(id: GameId, shift: Shift) {
            // do nothing
        }
    }
}