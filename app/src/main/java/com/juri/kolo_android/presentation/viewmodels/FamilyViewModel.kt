package com.juri.kolo_android.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juri.kolo_android.data.model.CreateFamilyBody
import com.juri.kolo_android.data.model.JoinFamilyBody
import com.juri.kolo_android.data.repository.Repository
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    val user = repository.user

    private val _dataState = MutableLiveData<DataState?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage get() = _errorMessage

    fun createFamily(createFamilyBody: CreateFamilyBody, userId: Int) {
        viewModelScope.launch {
            _dataState.value = DataState.LOADING

            when(val result = repository.createFamily(createFamilyBody, userId)) {
                is NetworkResult.Success -> {
                    _dataState.value = DataState.SUCCESS
                }
                is NetworkResult.Error -> {
                    _errorMessage.value = result.exception
                    _dataState.value = DataState.ERROR
                }
                else -> {
                    _dataState.value = DataState.LOADING
                }
            }
        }
    }

    fun joinFamily(joinFamilyBody: JoinFamilyBody, userId: Int) {
        viewModelScope.launch {
            _dataState.value = DataState.LOADING
            when(val result = repository.joinFamily(joinFamilyBody, userId)) {
                is NetworkResult.Success -> {
                    _dataState.value = DataState.SUCCESS
                }
                is NetworkResult.Error -> {
                    _errorMessage.value = result.exception
                    _dataState.value = DataState.ERROR
                }
                else -> {
                    _dataState.value = DataState.LOADING
                }
            }
        }
    }
}