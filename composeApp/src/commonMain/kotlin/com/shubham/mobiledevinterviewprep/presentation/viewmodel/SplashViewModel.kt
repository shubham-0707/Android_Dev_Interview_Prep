package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageAuthUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data class Navigate(val isLoggedIn: Boolean) : SplashUiState()
}

class SplashViewModel(
    private val manageAuthUseCase: ManageAuthUseCase,
    private val manageProgressUseCase: ManageProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    fun checkAuth() {
        viewModelScope.launch {
            val user = manageAuthUseCase.currentUser().first()
            if (user != null) {
                try {
                    manageProgressUseCase.syncFromRemoteIfLoggedIn()
                } catch (_: Exception) {
                    // Ignore offline sync errors
                }
            }
            _uiState.value = SplashUiState.Navigate(user != null)
        }
    }
}
