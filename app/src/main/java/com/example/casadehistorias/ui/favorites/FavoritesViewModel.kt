package com.example.casadehistorias.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casadehistorias.data.firebase.AuthRepository
import com.example.casadehistorias.data.repository.StoryRepository
import com.example.casadehistorias.domain.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Story>>(emptyList())
    val favorites: StateFlow<List<Story>> = _favorites.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            val user = authRepository.currentUser
            if (user != null) {
                val profile = authRepository.getUserProfile(user.uid)
                val ids = profile?.favoriteStories ?: emptyList()
                _favoriteIds.value = ids.toSet()
                if (ids.isNotEmpty()) {
                    _favorites.value = storyRepository.getStoriesByIds(ids)
                } else {
                    _favorites.value = emptyList()
                }
            }
            _isLoading.value = false
        }
    }

    fun toggleFavorite(storyId: String) {
        viewModelScope.launch {
            val isFav = _favoriteIds.value.contains(storyId)
            if (isFav) {
                storyRepository.removeFromFavorites(storyId)
                _favoriteIds.value = _favoriteIds.value - storyId
                _favorites.value = _favorites.value.filter { it.id != storyId }
            } else {
                storyRepository.addToFavorites(storyId)
                _favoriteIds.value = _favoriteIds.value + storyId
                val story = storyRepository.getStoryById(storyId)
                if (story != null) {
                    _favorites.value = _favorites.value + story
                }
            }
        }
    }

    fun isFavorite(storyId: String): Boolean {
        return _favoriteIds.value.contains(storyId)
    }
}
