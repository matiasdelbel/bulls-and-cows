package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.session.domain.SessionId

interface SessionIdRepository {

    suspend fun obtainCurrentOrCreate(creator: suspend () -> SessionId): SessionId

    fun removeCurrent()
}