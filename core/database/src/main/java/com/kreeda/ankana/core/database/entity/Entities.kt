package com.kreeda.ankana.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String?,
    val village: String,
    val isAnonymous: Boolean,
    val updatedAt: Long
)

@Entity(tableName = "grounds")
data class GroundEntity(
    @PrimaryKey val groundId: String,
    val name: String,
    val locationText: String,
    val sportTypesCsv: String,
    val isActive: Boolean,
    val updatedAt: Long
)

@Entity(tableName = "slots")
data class SlotEntity(
    @PrimaryKey val slotId: String,
    val groundId: String,
    val startEpochMs: Long,
    val endEpochMs: Long,
    val status: String,
    val updatedAt: Long
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val bookingId: String,
    val groundId: String,
    val slotId: String,
    val userUid: String,
    val teamName: String,
    val sportType: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: String = "PENDING"
)

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val challengeId: String,
    val authorUid: String,
    val groundId: String,
    val sportType: String,
    val scheduledDate: String,
    val message: String,
    val status: String,
    val createdAt: Long
)

@Entity(tableName = "replies")
data class ReplyEntity(
    @PrimaryKey val replyId: String,
    val challengeId: String,
    val authorUid: String,
    val message: String,
    val createdAt: Long
)

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey val scoreId: String,
    val bookingId: String,
    val groundId: String,
    val sportType: String,
    val teamA: String,
    val teamB: String,
    val resultSummary: String,
    val authorUid: String,
    val createdAt: Long
)
