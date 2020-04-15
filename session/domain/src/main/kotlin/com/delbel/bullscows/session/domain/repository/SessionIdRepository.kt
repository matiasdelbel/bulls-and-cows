package com.delbel.bullscows.session.domain.repository

import com.delbel.bullscows.session.domain.SessionId

interface SessionIdRepository {

    fun registerCurrent(id: SessionId)

    suspend fun obtainCurrentOrCreate(creator: suspend () -> SessionId): SessionId

    suspend fun obtainCurrentOrThrow(exception: Exception): SessionId

    fun removeCurrent()
}