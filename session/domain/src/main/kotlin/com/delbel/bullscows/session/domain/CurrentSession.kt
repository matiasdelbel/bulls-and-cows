package com.delbel.bullscows.session.domain

interface CurrentSession {

    suspend fun id(): SessionId

    fun update(id: SessionId)
}