package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.Challenge
import com.kreeda.ankana.core.model.Reply
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@Singleton
class ChallengesRepositoryImpl @Inject constructor() : ChallengesRepository {
    private val defaultChallenges = listOf(
        Challenge("c1", "user_demo_1", "ground_01", "Cricket", "2026-05-20", "Looking for a competitive 11-a-side match.", "OPEN", true),
        Challenge("c2", "user_demo_2", "ground_02", "Football", "2026-05-22", "5v5 match this weekend. Need opponents.", "OPEN")
    )
    private val defaultReplies = listOf(
        Reply("r1", "c1", "user_demo_3", "We are interested! Let's connect.", System.currentTimeMillis())
    )
    private val challenges = MutableStateFlow<List<Challenge>>(defaultChallenges)
    private val replies = MutableStateFlow<List<Reply>>(defaultReplies)

    override fun observeChallenges(): Flow<List<Challenge>> = challenges

    override fun observeReplies(challengeId: String): Flow<List<Reply>> {
        return replies.map { all -> all.filter { it.challengeId == challengeId } }
    }

    override suspend fun createChallenge(challenge: Challenge): Result<Unit> = runCatching {
        challenges.value = listOf(challenge) + challenges.value
    }

    override suspend fun updateChallenge(challenge: Challenge): Result<Unit> = runCatching {
        challenges.value = challenges.value.map { if (it.challengeId == challenge.challengeId) challenge else it }
    }

    override suspend fun replyToChallenge(reply: Reply): Result<Unit> = runCatching {
        replies.value = listOf(reply) + replies.value
    }
}
