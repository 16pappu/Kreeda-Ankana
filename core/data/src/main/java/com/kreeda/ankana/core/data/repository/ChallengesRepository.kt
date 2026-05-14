package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.Challenge
import com.kreeda.ankana.core.model.Reply
import kotlinx.coroutines.flow.Flow

interface ChallengesRepository {
    fun observeChallenges(): Flow<List<Challenge>>
    fun observeReplies(challengeId: String): Flow<List<Reply>>
    suspend fun createChallenge(challenge: Challenge): Result<Unit>
    suspend fun updateChallenge(challenge: Challenge): Result<Unit>
    suspend fun replyToChallenge(reply: Reply): Result<Unit>
}
