package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.database.dao.BookingDao
import com.kreeda.ankana.core.database.entity.BookingEntity
import com.kreeda.ankana.core.model.Booking
import com.kreeda.ankana.core.model.BookingStatus
import com.kreeda.ankana.core.network.FirestoreService
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val bookingDao: BookingDao,
    private val firestoreService: FirestoreService
) : BookingRepository {

    override fun observeBookings(): Flow<List<Booking>> {
        return bookingDao.observeBookings().map { entities ->
            entities.map { entity ->
                Booking(
                    bookingId = entity.bookingId,
                    groundId = entity.groundId,
                    slotId = entity.slotId,
                    userUid = entity.userUid,
                    teamName = entity.teamName,
                    sportType = entity.sportType,
                    status = BookingStatus.valueOf(entity.status),
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt
                )
            }
        }
    }

    override suspend fun createBooking(
        bookingId: String,
        groundId: String,
        slotId: String,
        userUid: String,
        teamName: String,
        sportType: String
    ): Result<Unit> {
        val createdAt = System.currentTimeMillis()
        bookingDao.upsert(
            BookingEntity(
                bookingId = bookingId,
                groundId = groundId,
                slotId = slotId,
                userUid = userUid,
                teamName = teamName,
                sportType = sportType,
                status = BookingStatus.PENDING.name,
                createdAt = createdAt,
                updatedAt = createdAt,
                syncState = "PENDING"
            )
        )

        return firestoreService.bookSlotTransaction(
            bookingId = bookingId,
            groundId = groundId,
            slotId = slotId,
            userUid = userUid,
            teamName = teamName,
            sportType = sportType
        ).onSuccess {
            bookingDao.upsert(
                BookingEntity(
                    bookingId = bookingId,
                    groundId = groundId,
                    slotId = slotId,
                    userUid = userUid,
                    teamName = teamName,
                    sportType = sportType,
                    status = BookingStatus.CONFIRMED.name,
                    createdAt = createdAt,
                    updatedAt = System.currentTimeMillis(),
                    syncState = "SYNCED"
                )
            )
        }.onFailure {
            bookingDao.upsert(
                BookingEntity(
                    bookingId = bookingId,
                    groundId = groundId,
                    slotId = slotId,
                    userUid = userUid,
                    teamName = teamName,
                    sportType = sportType,
                    status = BookingStatus.CONFLICT.name,
                    createdAt = createdAt,
                    updatedAt = System.currentTimeMillis(),
                    syncState = "FAILED"
                )
            )
        }
    }
}
