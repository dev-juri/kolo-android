package com.juri.kolo_android.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juri.kolo_android.data.model.LoginBody
import com.juri.kolo_android.data.model.RegisterBody
import com.juri.kolo_android.data.repository.Repository
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _dataState = MutableLiveData<DataState?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage get() = _errorMessage

    fun login(loginBody: LoginBody) {
        viewModelScope.launch {
            _dataState.value = DataState.LOADING
            when (val result = repository.loginUser(loginBody)) {
                is NetworkResult.Success -> {
                    _errorMessage.value = ""
                    _dataState.value = DataState.SUCCESS
                }

                is NetworkResult.Error -> {
                    _dataState.value = DataState.ERROR
                    _errorMessage.value = result.exception
                }

                else -> {
                    _dataState.value = DataState.LOADING
                }
            }
        }
    }

    fun register(registerBody: RegisterBody) {
        viewModelScope.launch {
            _dataState.value = DataState.LOADING
            when (val result = repository.registerUser(registerBody)) {
                is NetworkResult.Success -> {
                    _errorMessage.value = ""
                    _dataState.value = DataState.SUCCESS
                }

                is NetworkResult.Error -> {
                    _dataState.value = DataState.ERROR
                    _errorMessage.value = result.exception
                }

                else -> {
                    _dataState.value = DataState.LOADING
                }
            }
        }
    }
}