package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.domain.core.Answer
import com.delbel.bullscows.game.domain.core.Guess
import com.delbel.bullscows.game.domain.core.Secret

data class Game(private val secret: Secret, private val maxAttempts: Int) {

    private lateinit var current: Shift

    fun guess(guess: Guess): Shift {
        val answer = secret.guess(guess)
        updateCurrentShift(newAnswer = answer)

        return current
    }

    private fun updateCurrentShift(newAnswer: Answer) {
        current = if (::current.isInitialized) current.next(answer = newAnswer)
        else Shift(answer = newAnswer, maxAttempts = maxAttempts)
    }
}