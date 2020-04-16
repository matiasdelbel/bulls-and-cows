package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.exception.GameNoStartedException
import com.delbel.bullscows.game.domain.exception.GameNoWonException
import java.lang.Long.MAX_VALUE

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

    fun points(): Int {
        checkIfIsWinningShift()

        return current?.points() ?: throw GameNoStartedException()
    }

    fun throwIfIsOver(exception: Exception) =
        current?.executeIf(lost = { throw exception }, won = { throw exception })

    private fun nextShift(guess: Guess, answer: Answer) = current?.next(guess, answer)
        ?: Shift(answer = answer, guess = guess, maxAttempts = maxAttempts)

    private fun checkIfIsWinningShift() = current?.executeIf(
        lost = { throw GameNoWonException() },
        inProgress = { throw GameNoWonException() }
    )
}

inline class GameId(val id: Long = MAX_VALUE)