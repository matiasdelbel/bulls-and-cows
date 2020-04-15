package com.delbel.bullscows.game.gateway

import com.delbel.bullscows.game.domain.Game
import com.delbel.bullscows.game.domain.GameId
import com.delbel.bullscows.game.domain.Shift
import com.delbel.bullscows.game.domain.core.Secret
import com.delbel.bullscows.game.domain.repository.GameRepository
import com.delbel.bullscows.game.gateway.database.GameDao
import com.delbel.bullscows.game.gateway.database.ShiftDao
import com.delbel.bullscows.game.gateway.factory.GameFactory
import com.delbel.bullscows.game.gateway.model.GameDo
import com.delbel.bullscows.game.gateway.model.ShiftDo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalGameRepository @Inject constructor(
    private val gameDao: GameDao,
    private val shiftDao: ShiftDao,
    private val gameFactory: GameFactory
) : GameRepository {

    override suspend fun create(): GameId {
        val game = gameFactory.create()
        val id = gameDao.insert(game = GameDo.createFrom(game))

        return GameId(id = id)
    }

    override suspend fun obtainGameBy(id: GameId): Game {
        val gameDo = gameDao.obtainBy(gameId = id.id)
        val lastShiftDo = shiftDao.obtainLastFor(gameId = id.id)?.asModel()

        return gameDo.asModel(currentShift = lastShiftDo)
    }

    override suspend fun addShift(gameId: GameId, shift: Shift) =
        shiftDao.insert(shift = ShiftDo.createFrom(gameId, shift))

    override fun shiftsFor(id: GameId): Flow<List<Shift>> =
        shiftDao.obtainFor(gameId = id.id).map { shifts -> shifts.map { it.asModel() } }
}