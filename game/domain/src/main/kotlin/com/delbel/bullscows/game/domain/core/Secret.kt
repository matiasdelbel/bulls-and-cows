package com.delbel.bullscows.game.domain.core

data class Secret(val first: Int, val second: Int, val third: Int, val fourth: Int) {

    init {
        val secret = listOf(first, second, third, fourth).distinct()
        require(secret.count() == 4) { "The four-digit secret number must be all different." }
    }

    fun guess(guess: Guess) = guess.guess(first, second, third, fourth)
}