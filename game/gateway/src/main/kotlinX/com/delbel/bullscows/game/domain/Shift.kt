package com.delbel.bullscows.game.domain

import com.delbel.bullscows.game.gateway.model.ShiftDo

internal fun Shift.toDo(gameId: Long) = ShiftDo(
    gameId = gameId,
    attempt = attempt,
    guess = with(guess) { ShiftDo.GuessDo(first, second, third, fourth) },
    bulls = answer.bulls,
    cows = answer.cows,
    maxAttempts = maxAttempts
)