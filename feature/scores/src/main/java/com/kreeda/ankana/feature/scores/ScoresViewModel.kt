package com.kreeda.ankana.feature.scores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreeda.ankana.core.data.repository.ScoreRepository
import com.kreeda.ankana.core.model.Score
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.kreeda.ankana.core.data.repository.AuthRepository
import com.kreeda.ankana.core.network.FirestoreService

@HiltViewModel
class ScoresViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
    private val authRepository: AuthRepository,
    private val firestoreService: FirestoreService
) : ViewModel() {
    val scores: StateFlow<List<Score>> = scoreRepository.observeScores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun submitScore(teamA: String, teamB: String, resultSummary: String) {
        viewModelScope.launch {
            val authorUid = authRepository.currentUserId.firstOrNull() ?: "local_demo_user"
            
            scoreRepository.submitScore(
                Score(
                    scoreId = "score_${System.currentTimeMillis()}",
                    bookingId = "booking_demo",
                    groundId = "ground_01",
                    sportType = "Cricket",
                    teamA = teamA,
                    teamB = teamB,
                    resultSummary = resultSummary,
                    authorUid = authorUid
                )
            )

            // Increment wins for the author (assuming author's team won)
            firestoreService.incrementWinStats(authorUid)
        }
    }
}
