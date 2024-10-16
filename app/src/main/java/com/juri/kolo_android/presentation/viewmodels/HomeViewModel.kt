package com.juri.kolo_android.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juri.kolo_android.data.repository.Repository
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _dataState = MutableLiveData<DataState?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage get() = _errorMessage

    val user = repository.user

    val transactions = repository.txns

    val family = repository.family

    fun fetchTxns(userId: Int) {
        viewModelScope.launch {
            when (repository.fetchTransactions(userId)) {
                is NetworkResult.Success -> {
                    _dataState.value = DataState.SUCCESS
                }

                is NetworkResult.Error -> {
                    _dataState.value = DataState.ERROR
                }

                else -> {
                    _dataState.value = DataState.LOADING
                }
            }

        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}