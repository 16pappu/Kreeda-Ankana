package com.kreeda.ankana.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreeda.ankana.core.data.repository.ProfileRepository
import com.kreeda.ankana.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.kreeda.ankana.core.data.repository.AuthRepository

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    val profile: StateFlow<User?> = profileRepository.observeProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun saveProfile(displayName: String, teamName: String, role: String, sportsStr: String) {
        viewModelScope.launch {
            val sportsList = sportsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            profileRepository.updateProfile(displayName, teamName, role, sportsList)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
