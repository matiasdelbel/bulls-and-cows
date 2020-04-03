package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret

data class Game(
    private val id: GameId,
    private val secret: Secret,
    private val maxAttempts: Int,
    private var current: Shift? = null
) {

    fun guess(guess: Guess): Shift {
        val answer = secret.guess(guess)
        val next = nextShift(guess = guess, answer = answer)
        current = next

        return next
    }

    private fun nextShift(guess: Guess, answer: Answer) = current?.next(guess, answer)
        ?: Shift(answer = answer, guess = guess, maxAttempts = maxAttempts)
}

inline class GameId(val id: String)