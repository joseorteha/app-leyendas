package com.example.casadehistorias.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import com.example.casadehistorias.data.firebase.StorageRepository
import com.example.casadehistorias.data.repository.StoryRepository
import com.example.casadehistorias.domain.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: StoryRepository,
    private val storageRepository: StorageRepository
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
        imageUri: Uri? = null,
        audioUri: Uri? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        tags: List<String> = emptyList()
    ) {
        viewModelScope.launch {
            _uiState.value = AddStoryUiState.Loading
            
            val tempId = UUID.randomUUID().toString()
            var uploadedImgUrl: String? = null
            if (imageUri != null) {
                uploadedImgUrl = storageRepository.uploadImage(imageUri, tempId)
            }

            val newStory = Story(
                id = "",
                titleEs = titleEs,
                titleNahuatl = titleNahuatl,
                contentEs = contentEs,
                contentNahuatl = contentNahuatl,
                audioUrl = null,
                imageUrl = uploadedImgUrl,
                narratorName = narratorName,
                community = community,
                latitude = latitude,
                longitude = longitude,
                tags = tags
            )

            val resultId = repository.addStory(newStory)
            if (resultId != null) {
                _uiState.value = AddStoryUiState.Success
            } else {
                _uiState.value = AddStoryUiState.Error("No se pudo guardar la historia. Verifica tu conexión.")
            }
        }
    }

    fun updateStory(story: Story, newImageUri: Uri? = null) {
        viewModelScope.launch {
            _uiState.value = AddStoryUiState.Loading
            
            var finalStory = story
            if (newImageUri != null) {
                // Upload new image and replace the url
                val uploadedImgUrl = storageRepository.uploadImage(newImageUri, story.id)
                if (uploadedImgUrl != null) {
                    finalStory = story.copy(imageUrl = uploadedImgUrl)
                }
            }
            
            val success = repository.updateStory(finalStory)
            if (success) {
                _uiState.value = AddStoryUiState.Success
            } else {
                _uiState.value = AddStoryUiState.Error("No se pudo actualizar la historia.")
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
