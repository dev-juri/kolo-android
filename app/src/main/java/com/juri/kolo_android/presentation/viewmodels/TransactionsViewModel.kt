package com.juri.kolo_android.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juri.kolo_android.data.model.DepositBody
import com.juri.kolo_android.data.model.WithdrawBody
import com.juri.kolo_android.data.repository.Repository
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _dataState = MutableLiveData<DataState?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage get() = _errorMessage

    private val _authorizationUrl = MutableLiveData<String?>()
    val authorizationUrl get() = _authorizationUrl

    val user = repository.user

    fun deposit(depositBody: DepositBody) {
        viewModelScope.launch {
            _dataState.value = DataState.LOADING

            when (val result = repository.deposit(depositBody)) {
                is NetworkResult.Success -> {
                    _dataState.value = DataState.SUCCESS
                    _authorizationUrl.value = result.data.data.authorizationUrl
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

    fun withdraw(withdrawBody: WithdrawBody) {
        viewModelScope.launch {
            _dataState.value = DataState.LOADING

            when (val result = repository.withdraw(withdrawBody)) {
                is NetworkResult.Success -> {
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

    fun reset() {
        _authorizationUrl.value = null
        _errorMessage.value = ""
        _dataState.value = null
    }
}