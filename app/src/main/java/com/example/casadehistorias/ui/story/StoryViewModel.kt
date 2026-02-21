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

    init {
        loadStories()
    }

    private fun loadStories() {
        viewModelScope.launch {
            repository.getAllStoriesFlow()
                .catch { e ->
                    Log.e("StoryViewModel", "Error cargando historias: ${e.message}")
                    // Aquí podrías mostrar un mensaje de error en la UI
                }
                .collect { storyList ->
                    _stories.value = storyList
                }
        }
    }

    fun getStoryById(id: String): Story? {
        return _stories.value.find { it.id == id }
    }
}
