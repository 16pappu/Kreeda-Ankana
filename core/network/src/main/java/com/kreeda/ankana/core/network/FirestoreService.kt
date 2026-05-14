package com.kreeda.ankana.core.network

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun bookSlotTransaction(
        bookingId: String,
        groundId: String,
        slotId: String,
        userUid: String,
        teamName: String,
        sportType: String
    ): Result<Unit> = runCatching {
        val slotRef = firestore.collection("grounds")
            .document(groundId)
            .collection("slots")
            .document(slotId)

        val bookingRef = firestore.collection("bookings").document(bookingId)

        firestore.runTransaction { transaction ->
            val slotSnapshot = transaction.get(slotRef)
            val status = slotSnapshot.getString("status") ?: "AVAILABLE"
            if (status != "AVAILABLE") {
                throw IllegalStateException("SLOT_TAKEN")
            }

            transaction.update(slotRef, mapOf("status" to "BOOKED"))
            transaction.set(
                bookingRef,
                mapOf(
                    "bookingId" to bookingId,
                    "groundId" to groundId,
                    "slotId" to slotId,
                    "userUid" to userUid,
                    "teamName" to teamName,
                    "sportType" to sportType,
                    "status" to "CONFIRMED",
                    "createdAt" to System.currentTimeMillis(),
                    "updatedAt" to System.currentTimeMillis()
                )
            )
        }.await()
    }

    suspend fun createUserProfile(user: com.kreeda.ankana.core.model.User): Result<Unit> = runCatching {
        firestore.collection("users")
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun getUserProfile(uid: String): Result<com.kreeda.ankana.core.model.User?> = runCatching {
        val document = firestore.collection("users")
            .document(uid)
            .get()
            .await()
        document.toObject(com.kreeda.ankana.core.model.User::class.java)
    }
    suspend fun incrementWinStats(uid: String): Result<Unit> = runCatching {
        val userRef = firestore.collection("users").document(uid)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentWins = snapshot.getLong("stats.wins") ?: 0L
            val currentMatches = snapshot.getLong("stats.matchesPlayed") ?: 0L
            transaction.update(
                userRef,
                mapOf(
                    "stats.wins" to currentWins + 1,
                    "stats.matchesPlayed" to currentMatches + 1
                )
            )
        }.await()
    }
}
