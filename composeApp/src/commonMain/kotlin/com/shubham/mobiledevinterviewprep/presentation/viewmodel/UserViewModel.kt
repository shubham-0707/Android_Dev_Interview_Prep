package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageAuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UserUiState {
    data object Idle : UserUiState()
    data object LoggedOut : UserUiState()
}

class UserViewModel(
    private val manageAuthUseCase: ManageAuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            manageAuthUseCase.logout()
            _uiState.value = UserUiState.LoggedOut
        }
    }
}
