package com.kreeda.ankana.feature.grounds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreeda.ankana.core.data.repository.GroundsRepository
import com.kreeda.ankana.core.model.Ground
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class GroundsViewModel @Inject constructor(
    private val groundsRepository: GroundsRepository
) : ViewModel() {
    val grounds = groundsRepository.observeGrounds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedGround = MutableStateFlow<Ground?>(null)
    val selectedGround: StateFlow<Ground?> = _selectedGround.asStateFlow()

    fun selectGround(ground: Ground) {
        _selectedGround.value = ground
    }

    fun addGround(name: String, locationText: String, sportTypes: List<String>) {
        viewModelScope.launch {
            val newGround = Ground(
                groundId = UUID.randomUUID().toString(),
                name = name,
                locationText = locationText,
                sportTypes = sportTypes,
                isActive = true
            )
            groundsRepository.addGround(newGround)
        }
    }
}
