package com.example.casadehistorias.data.firebase

import com.example.casadehistorias.data.firebase.model.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    val currentUser: FirebaseUser? get() = auth.currentUser

    val authState: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            updateLastLogin(result.user?.uid)
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUserWithEmailAndPassword(
        email: String, 
        password: String,
        displayName: String,
        community: String
    ): Result<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                createUserProfile(user.uid, email, displayName, community)
            }
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            document.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfile(userId: String, profile: UserProfile): Boolean {
        return try {
            firestore.collection("users")
                .document(userId)
                .set(profile.copy(id = userId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun createUserProfile(
        userId: String, 
        email: String, 
        displayName: String,
        community: String
    ) {
        val profile = UserProfile(
            id = userId,
            email = email,
            displayName = displayName,
            community = community,
            createdAt = Timestamp.now(),
            lastLogin = Timestamp.now()
        )
        firestore.collection("users").document(userId).set(profile).await()
    }

    private suspend fun updateLastLogin(userId: String?) {
        userId?.let {
            firestore.collection("users")
                .document(it)
                .update("lastLogin", Timestamp.now())
                .await()
        }
    }
}
