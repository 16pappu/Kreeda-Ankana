package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.Booking
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun observeBookings(): Flow<List<Booking>>

    suspend fun createBooking(
        bookingId: String,
        groundId: String,
        slotId: String,
        userUid: String,
        teamName: String,
        sportType: String
    ): Result<Unit>
}
