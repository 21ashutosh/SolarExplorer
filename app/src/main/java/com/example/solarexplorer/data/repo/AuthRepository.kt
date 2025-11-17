package com.example.solarexplorer.data.repo

import com.example.solarexplorer.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

sealed class AuthResultState {
    object Loading : AuthResultState()
    data class Success(val uid: String) : AuthResultState()
    data class Failure(val message: String) : AuthResultState()
}

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // Register with email & password, then save profile in Firestore under "users/{uid}"
    suspend fun registerAndSaveProfile(name: String, email: String, password: String, phone: String): AuthResultState {
        return try {
            // Create user in Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Failed to get uid")

            // Build user profile
            val profile = UserProfile(uid = uid, name = name, email = email, phone = phone, createdAt = System.currentTimeMillis())

            // Save in Firestore
            firestore.collection("users")
                .document(uid)
                .set(profile)
                .await()

            AuthResultState.Success(uid)
        } catch (e: Exception) {
            AuthResultState.Failure(e.message ?: "Registration failed")
        }
    }

    // Login
    suspend fun login(email: String, password: String): AuthResultState {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Failed to get uid")
            AuthResultState.Success(uid)
        } catch (e: Exception) {
            AuthResultState.Failure(e.message ?: "Login failed")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    // Optional: fetch profile
    suspend fun getProfile(uid: String): UserProfile? {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            if (doc.exists()) doc.toObject(UserProfile::class.java) else null
        } catch (e: Exception) {
            null
        }
    }
}
