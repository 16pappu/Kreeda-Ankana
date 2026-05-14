package com.kreeda.ankana.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUserId: Flow<String?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(authStateListener)
        
        // Initial state
        trySend(auth.currentUser?.uid)
        
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signInAnonymously(): Result<String> = runCatching {
        val result = auth.signInAnonymously().await()
        result.user?.uid ?: throw Exception("Anonymous sign in failed: User is null")
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<String> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("User is null after sign in")
        Result.success(uid)
    } catch (e: FirebaseAuthException) {
        val errorMessage = when (e.errorCode) {
            "ERROR_INVALID_EMAIL" -> "The email address is badly formatted."
            "ERROR_USER_NOT_FOUND" -> "No user found with this email."
            "ERROR_WRONG_PASSWORD" -> "Incorrect password."
            "ERROR_USER_DISABLED" -> "This user account has been disabled."
            else -> e.localizedMessage ?: "Authentication failed."
        }
        Result.failure(Exception(errorMessage, e))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<String> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("User is null after sign up")
        Result.success(uid)
    } catch (e: FirebaseAuthException) {
        val errorMessage = when (e.errorCode) {
            "ERROR_INVALID_EMAIL" -> "The email address is badly formatted."
            "ERROR_EMAIL_ALREADY_IN_USE" -> "The email address is already in use by another account."
            "ERROR_WEAK_PASSWORD" -> "The password must be 6 characters long or more."
            else -> e.localizedMessage ?: "Sign up failed."
        }
        Result.failure(Exception(errorMessage, e))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
