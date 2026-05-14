package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {
    fun observeScores(): Flow<List<Score>>
    suspend fun submitScore(score: Score): Result<Unit>
}
