package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.model.UserProfile
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageAuthUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class UserUiState {
    data class Ready(val user: UserProfile?) : UserUiState()
    data object LoggedOut : UserUiState()
}

class UserViewModel(
    private val manageAuthUseCase: ManageAuthUseCase,
    private val manageProgressUseCase: ManageProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Ready(null))
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            manageAuthUseCase.currentUser().collectLatest { user ->
                _uiState.value = UserUiState.Ready(user)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            manageAuthUseCase.signOut()
            manageProgressUseCase.clearProgress()
            _uiState.value = UserUiState.LoggedOut
        }
    }

    fun updatePhoto(bytes: ByteArray) {
        viewModelScope.launch {
            manageAuthUseCase.updateProfilePhoto(bytes)
        }
    }
}
