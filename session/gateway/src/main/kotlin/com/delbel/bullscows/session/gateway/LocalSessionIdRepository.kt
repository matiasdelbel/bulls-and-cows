package com.delbel.bullscows.session.gateway

import android.content.SharedPreferences
import com.delbel.bullscows.session.domain.SessionId
import com.delbel.bullscows.session.domain.repository.SessionIdRepository
import java.lang.Long.MIN_VALUE
import javax.inject.Inject

internal class LocalSessionIdRepository @Inject constructor(
    private val preferences: SharedPreferences
) : SessionIdRepository {

    companion object {
        private const val CURRENT_SESSION_ID = "CURRENT_SESSION_ID"
    }

    override suspend fun obtainCurrentOrCreate(creator: suspend () -> SessionId): SessionId {
        val sessionId = obtainCurrent()
        if (sessionId.value == MIN_VALUE) updateCurrent(id = creator())

        return obtainCurrent()
    }

    override fun removeCurrent() = updateCurrent(id = SessionId(MIN_VALUE))

    private fun obtainCurrent() = SessionId(value = preferences.getLong(CURRENT_SESSION_ID, MIN_VALUE))

    private fun updateCurrent(id: SessionId) = preferences.edit().putLong(CURRENT_SESSION_ID, id.value).apply()
}