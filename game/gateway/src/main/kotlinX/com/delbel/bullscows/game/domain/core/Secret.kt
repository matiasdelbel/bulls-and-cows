package com.delbel.bullscows.game.domain.core

import com.delbel.bullscows.game.gateway.model.GameDo

internal fun Secret.toDo() = GameDo.SecretDo(
    first = first,
    second = second,
    third = third,
    fourth = fourth
)