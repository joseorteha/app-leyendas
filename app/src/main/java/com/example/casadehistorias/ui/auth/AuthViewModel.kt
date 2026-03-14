package com.example.casadehistorias.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casadehistorias.data.firebase.AuthRepository
import com.example.casadehistorias.data.firebase.model.UserProfile
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    // Check if user is already logged in (for determining start destination)
    val isLoggedIn: Boolean get() = authRepository.currentUser != null

    init {
        viewModelScope.launch {
            authRepository.authState.collect { user ->
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                    loadUserProfile(user.uid)
                } else {
                    _authState.value = AuthState.Unauthenticated
                    _userProfile.value = null
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithEmailAndPassword(email, password)
            if (result.isFailure) {
                _authState.value = AuthState.Error(
                    result.exceptionOrNull()?.message ?: "Error desconocido"
                )
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String, community: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.createUserWithEmailAndPassword(
                email, password, displayName, community
            )
            if (result.isFailure) {
                _authState.value = AuthState.Error(
                    result.exceptionOrNull()?.message ?: "Error desconocido"
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun updateProfile(displayName: String, community: String) {
        viewModelScope.launch {
            val user = authRepository.currentUser ?: return@launch
            val currentProfile = _userProfile.value ?: return@launch
            val updated = currentProfile.copy(
                displayName = displayName,
                community = community
            )
            if (authRepository.updateUserProfile(user.uid, updated)) {
                _userProfile.value = updated
            }
        }
    }

    private fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            val profile = authRepository.getUserProfile(userId)
            _userProfile.value = profile
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}
