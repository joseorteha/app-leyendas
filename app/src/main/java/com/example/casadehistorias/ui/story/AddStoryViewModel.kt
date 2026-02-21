package com.example.casadehistorias.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casadehistorias.data.repository.StoryRepository
import com.example.casadehistorias.domain.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddStoryUiState>(AddStoryUiState.Idle)
    val uiState: StateFlow<AddStoryUiState> = _uiState.asStateFlow()

    fun addStory(
        titleEs: String,
        titleNahuatl: String,
        contentEs: String,
        contentNahuatl: String,
        narratorName: String,
        community: String,
        tags: List<String> = emptyList()
    ) {
        viewModelScope.launch {
            _uiState.value = AddStoryUiState.Loading
            
            val newStory = Story(
                id = "", // Firebase generará el ID
                titleEs = titleEs,
                titleNahuatl = titleNahuatl,
                contentEs = contentEs,
                contentNahuatl = contentNahuatl,
                audioUrl = null,
                imageUrl = null,
                narratorName = narratorName,
                community = community
            )

            val resultId = repository.addStory(newStory)
            if (resultId != null) {
                _uiState.value = AddStoryUiState.Success
            } else {
                _uiState.value = AddStoryUiState.Error("No se pudo guardar la historia")
            }
        }
    }

    fun resetState() {
        _uiState.value = AddStoryUiState.Idle
    }
}

sealed class AddStoryUiState {
    object Idle : AddStoryUiState()
    object Loading : AddStoryUiState()
    object Success : AddStoryUiState()
    data class Error(val message: String) : AddStoryUiState()
}
