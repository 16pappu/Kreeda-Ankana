package com.kreeda.ankana.core.data.repository

import com.kreeda.ankana.core.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<User?>
    suspend fun updateProfile(displayName: String, teamName: String, role: String, sports: List<String>): Result<Unit>
}
