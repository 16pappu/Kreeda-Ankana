package com.kreeda.ankana.core.model

data class UserStats(
    val matchesPlayed: Int = 0,
    val wins: Int = 0
)

data class User(
    val uid: String,
    val displayName: String,
    val email: String?,
    val teamName: String,
    val role: String, // e.g. "CAPTAIN" or "PLAYER"
    val sports: List<String>,
    val stats: UserStats = UserStats(),
    val createdAt: Long = System.currentTimeMillis()
)

data class Ground(
    val groundId: String,
    val name: String,
    val locationText: String,
    val sportTypes: List<String>,
    val isActive: Boolean
)

data class Slot(
    val slotId: String,
    val groundId: String,
    val startEpochMs: Long,
    val endEpochMs: Long,
    val status: SlotStatus
)

enum class SlotStatus { AVAILABLE, BOOKED, BLOCKED }

data class Booking(
    val bookingId: String,
    val groundId: String,
    val slotId: String,
    val userUid: String,
    val teamName: String,
    val sportType: String,
    val status: BookingStatus,
    val createdAt: Long,
    val updatedAt: Long
)

enum class BookingStatus { PENDING, CONFIRMED, CONFLICT, CANCELLED }

data class Challenge(
    val challengeId: String,
    val authorUid: String,
    val groundId: String,
    val sportType: String,
    val scheduledDate: String,
    val message: String,
    val status: String,
    val umpireRequested: Boolean = false,
    val umpireUid: String? = null
)

data class Reply(
    val replyId: String,
    val challengeId: String,
    val authorUid: String,
    val message: String,
    val createdAt: Long
)

data class Score(
    val scoreId: String,
    val bookingId: String,
    val groundId: String,
    val sportType: String,
    val teamA: String,
    val teamB: String,
    val resultSummary: String,
    val authorUid: String
)
