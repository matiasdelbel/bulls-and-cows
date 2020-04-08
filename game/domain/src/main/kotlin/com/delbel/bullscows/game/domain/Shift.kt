package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.exception.GameEndedException
import com.delbel.bullscows.game.domain.exception.GameNoEndedException

data class Shift(val attempt: Int = 1, val guess: Guess, val answer: Answer, val maxAttempts: Int) {

    companion object {
        private const val POINTS_MULTIPLIER = 10
    }

    fun executeIf(
        won: () -> Unit = {},
        lost: () -> Unit = {},
        inProgress: () -> Unit = {}
    ) = answer.executeIf(
        guess = won,
        wrongGuess = { if (hasMoreGuesses()) inProgress() else lost() }
    )

    fun points(): Int = answer.executeIf(
        guess = { maxAttempts * POINTS_MULTIPLIER / attempt },
        wrongGuess = { if (hasMoreGuesses()) throw GameNoEndedException() else throw GameEndedException() }
    )

    internal fun next(guess: Guess, answer: Answer): Shift {
        if (!hasMoreGuesses()) throw GameEndedException()
        return copy(attempt = attempt + 1, guess = guess, answer = answer)
    }

    private fun hasMoreGuesses() = attempt < maxAttempts
}