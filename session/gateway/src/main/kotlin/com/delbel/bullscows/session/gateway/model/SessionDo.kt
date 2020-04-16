package com.delbel.bullscows.session.gateway.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.delbel.bullscows.session.domain.Session
import com.delbel.bullscows.session.domain.SessionId

@Entity(tableName = "session")
internal data class SessionDo(
    val guessed: Int = 0,
    val points: Int = 0
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    fun asModel() = Session(
        id = SessionId(value = id),
        guessed = guessed,
        points = points
    )

    companion object {

        fun createFrom(session: Session) =
            SessionDo(session.guessed, session.points).apply { id = session.id.value }
    }
}