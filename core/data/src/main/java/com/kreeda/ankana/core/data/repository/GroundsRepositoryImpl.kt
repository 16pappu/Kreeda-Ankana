package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.Ground
import com.kreeda.ankana.core.model.Slot
import com.kreeda.ankana.core.model.SlotStatus
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@Singleton
class GroundsRepositoryImpl @Inject constructor() : GroundsRepository {
    private val grounds = MutableStateFlow(
        listOf(
            Ground("ground_01", "Ankana Main Ground", "Near Panchayat Office", listOf("Cricket", "Volleyball"), true),
            Ground("ground_02", "River Side Field", "North Hamlet", listOf("Football", "Kabaddi"), true)
        )
    )

    private val slots = MutableStateFlow(
        listOf(
            Slot("slot_01", "ground_01", 1777562400000, 1777566000000, SlotStatus.AVAILABLE),
            Slot("slot_02", "ground_01", 1777566000000, 1777569600000, SlotStatus.BOOKED),
            Slot("slot_03", "ground_02", 1777562400000, 1777566000000, SlotStatus.AVAILABLE)
        )
    )

    override fun observeGrounds(): Flow<List<Ground>> = grounds

    override fun observeSlots(groundId: String): Flow<List<Slot>> =
        slots.map { all -> all.filter { it.groundId == groundId } }

    override suspend fun addGround(ground: Ground): Result<Unit> = runCatching {
        grounds.value = grounds.value + ground
    }
}
