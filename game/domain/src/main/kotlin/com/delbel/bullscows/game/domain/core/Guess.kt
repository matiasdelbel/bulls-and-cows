package com.delbel.bullscows.game.domain.core

data class Guess(val first: Int, val second: Int, val third: Int, val fourth: Int) {

    private val guess: List<Int> = listOf(first, second, third, fourth).distinct()

    init {
        if (guess.count() != 4) throw MalformedGuessException()
    }

    internal fun guess(first: Int, second: Int, third: Int, fourth: Int): Answer {
        val secret = listOf(first, second, third, fourth)

        val bulls = guess.filterIndexed { index, value -> value == secret[index] }.size
        val cows = guess.intersect(secret).size - bulls

        return Answer(bulls, cows)
    }
}

class MalformedGuessException() : RuntimeException()