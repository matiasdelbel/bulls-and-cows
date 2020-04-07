package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret

data class Game(
    val id: GameId = GameId(),
    val secret: Secret,
    val maxAttempts: Int,
    private var current: Shift? = null
) {

    fun guess(guess: Guess): Shift {
        val answer = secret.guess(guess)
        val next = nextShift(guess = guess, answer = answer)
        current = next

        return next
    }

    fun executeIf(
        isAboutToStart: () -> Unit,
        isInProgress: () -> Unit,
        hasWon: () -> Unit,
        hasLost: () -> Unit
    ) = when (current == null) {
        true -> isAboutToStart()
        false -> current?.executeIf(won = hasWon, over = hasLost, inProgress = isInProgress)
    }

    private fun nextShift(guess: Guess, answer: Answer) = current?.next(guess, answer)
        ?: Shift(answer = answer, guess = guess, maxAttempts = maxAttempts)

    interface Factory {

        fun create(): Game
    }

}

inline class GameId(val id: Long = Long.MAX_VALUE)