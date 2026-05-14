package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.Score
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Singleton
class ScoreRepositoryImpl @Inject constructor() : ScoreRepository {
    private val defaultScores = listOf(
        Score("s1", "b1", "ground_01", "Cricket", "Tech Titans", "Code Crushers", "Tech Titans won by 4 wickets", "user_demo_1"),
        Score("s2", "b2", "ground_02", "Football", "Dev Dynamos", "Agile Avengers", "Match drawn 2-2", "user_demo_2")
    )
    private val scores = MutableStateFlow<List<Score>>(defaultScores)

    override fun observeScores(): Flow<List<Score>> = scores

    override suspend fun submitScore(score: Score): Result<Unit> = runCatching {
        scores.value = listOf(score) + scores.value
    }
}
