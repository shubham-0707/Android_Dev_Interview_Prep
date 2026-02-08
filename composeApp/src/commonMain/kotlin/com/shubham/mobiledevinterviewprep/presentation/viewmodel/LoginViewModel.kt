package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageAuthUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val manageAuthUseCase: ManageAuthUseCase,
    private val manageProgressUseCase: ManageProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId: StateFlow<String?> = _verificationId.asStateFlow()

    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code.asStateFlow()

    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                manageAuthUseCase.signInWithGoogle()
                try {
                    manageProgressUseCase.syncFromRemoteIfLoggedIn()
                } catch (_: Exception) {
                    // Ignore offline sync errors
                }
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun loginWithPhone() {
        startPhoneVerification()
    }

    fun updatePhoneNumber(value: String) {
        _phoneNumber.value = value
    }

    fun updateCode(value: String) {
        _code.value = value
    }

    private fun startPhoneVerification() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val id = manageAuthUseCase.startPhoneVerification(_phoneNumber.value)
                _verificationId.value = id
                _uiState.value = LoginUiState.Idle
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Phone verification failed")
            }
        }
    }

    fun verifyCode() {
        val id = _verificationId.value ?: return
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                manageAuthUseCase.verifyPhoneCode(id, _code.value)
                try {
                    manageProgressUseCase.syncFromRemoteIfLoggedIn()
                } catch (_: Exception) {
                    // Ignore offline sync errors
                }
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Verification failed")
            }
        }
    }
}
