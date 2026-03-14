package com.example.casadehistorias.ui.story

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casadehistorias.data.repository.StoryRepository
import com.example.casadehistorias.domain.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _selectedStory = MutableStateFlow<Story?>(null)
    val selectedStory: StateFlow<Story?> = _selectedStory.asStateFlow()

    init {
        loadStories()
    }

    private fun loadStories() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.getAllStoriesFlow()
                .catch { e ->
                    Log.e("StoryViewModel", "Error cargando historias: ${e.message}")
                    _errorMessage.value = "Error al cargar historias: ${e.message}"
                    _isLoading.value = false
                }
                .collect { storyList ->
                    _stories.value = storyList
                    _isLoading.value = false
                }
        }
    }

    fun retry() {
        loadStories()
    }

    fun loadStoryById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // First try from local list
            val localStory = _stories.value.find { it.id == id }
            if (localStory != null) {
                _selectedStory.value = localStory
                _isLoading.value = false
                return@launch
            }
            // Otherwise fetch from Firestore
            val story = repository.getStoryById(id)
            _selectedStory.value = story
            _isLoading.value = false
        }
    }

    fun getStoryById(id: String): Story? {
        return _stories.value.find { it.id == id }
    }

    fun deleteStory(storyId: String) {
        viewModelScope.launch {
            repository.deleteStory(storyId)
        }
    }
}
