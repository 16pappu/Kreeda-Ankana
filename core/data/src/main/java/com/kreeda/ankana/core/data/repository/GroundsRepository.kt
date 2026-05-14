package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.Ground
import com.kreeda.ankana.core.model.Slot
import kotlinx.coroutines.flow.Flow

interface GroundsRepository {
    fun observeGrounds(): Flow<List<Ground>>
    fun observeSlots(groundId: String): Flow<List<Slot>>
    suspend fun addGround(ground: Ground): Result<Unit>
}
