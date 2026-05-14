package com.kreeda.ankana.feature.challenges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreeda.ankana.core.data.repository.ChallengesRepository
import com.kreeda.ankana.core.model.Challenge
import com.kreeda.ankana.core.model.Reply
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.kreeda.ankana.core.data.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    private val challengesRepository: ChallengesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    val challenges: StateFlow<List<Challenge>> = challengesRepository.observeChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun createChallenge(sportType: String, message: String, date: String, requestUmpire: Boolean) {
        viewModelScope.launch {
            val authorUid = authRepository.currentUserId.firstOrNull() ?: "local_demo_user"
            challengesRepository.createChallenge(
                Challenge(
                    challengeId = "challenge_${System.currentTimeMillis()}",
                    authorUid = authorUid,
                    groundId = "ground_01",
                    sportType = sportType,
                    scheduledDate = date,
                    message = message,
                    status = "OPEN",
                    umpireRequested = requestUmpire
                )
            )
        }
    }

    fun reply(challengeId: String, message: String) {
        viewModelScope.launch {
            val authorUid = authRepository.currentUserId.firstOrNull() ?: "local_demo_user"
            challengesRepository.replyToChallenge(
                Reply(
                    replyId = "reply_${System.currentTimeMillis()}",
                    challengeId = challengeId,
                    authorUid = authorUid,
                    message = message,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun acceptChallenge(challengeId: String) {
        viewModelScope.launch {
            val challenge = challenges.value.find { it.challengeId == challengeId }
            if (challenge != null) {
                val authorUid = authRepository.currentUserId.firstOrNull() ?: "local_demo_user"
                challengesRepository.replyToChallenge(
                    Reply(
                        replyId = "reply_${System.currentTimeMillis()}",
                        challengeId = challengeId,
                        authorUid = authorUid,
                        message = "ACCEPTED",
                        createdAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun claimUmpire(challengeId: String) {
        viewModelScope.launch {
            val challenge = challenges.value.find { it.challengeId == challengeId }
            if (challenge != null && challenge.umpireRequested && challenge.umpireUid == null) {
                val umpireUid = authRepository.currentUserId.firstOrNull() ?: "local_demo_user"
                val updatedChallenge = challenge.copy(umpireUid = umpireUid)
                challengesRepository.updateChallenge(updatedChallenge)
            }
        }
    }
}
