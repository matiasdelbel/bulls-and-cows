package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess

data class Shift(
    private val attempt: Int = 1,
    private val guess: Guess,
    private val answer: Answer,
    private val maxAttempts: Int
) {

    fun executeIf(won: () -> Unit, over: () -> Unit, inProgress: () -> Unit) = answer.executeIf(
        guess = won,
        wrongGuess = { if (hasMoreGuesses()) inProgress() else over() }
    )

    internal fun next(guess: Guess, answer: Answer): Shift {
        throwIfHasNoMoreGuesses(exception = GameAlreadyEndedException())
        return copy(attempt = attempt + 1, guess = guess, answer = answer)
    }

    private fun throwIfHasNoMoreGuesses(exception: Exception) {
        if (!hasMoreGuesses()) throw exception
    }

    private fun hasMoreGuesses() = attempt < maxAttempts
}

class GameAlreadyEndedException : RuntimeException()