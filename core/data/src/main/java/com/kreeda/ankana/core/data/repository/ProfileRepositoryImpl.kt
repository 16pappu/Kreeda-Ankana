package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.User
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Singleton
class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {
    private val profile = MutableStateFlow<User?>(
        User(
            uid = "local_demo_user",
            displayName = "Village Player",
            email = null,
            teamName = "Ankana Heroes",
            role = "CAPTAIN",
            sports = listOf("Cricket")
        )
    )

    override fun observeProfile(): Flow<User?> = profile

    override suspend fun updateProfile(
        displayName: String,
        teamName: String,
        role: String,
        sports: List<String>
    ): Result<Unit> = runCatching {
        val existing = profile.value ?: return@runCatching
        profile.value = existing.copy(
            displayName = displayName,
            teamName = teamName,
            role = role,
            sports = sports
        )
    }
}
