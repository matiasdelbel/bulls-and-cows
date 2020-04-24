package com.delbel.bullscows.session.domain

import com.delbel.bullscows.session.gateway.model.SessionDo

internal fun Session.toDo() = SessionDo(guessed, points).apply { id = this@toDo.id.value }
