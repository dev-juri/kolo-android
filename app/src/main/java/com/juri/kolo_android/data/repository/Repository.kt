package com.juri.kolo_android.data.repository

import androidx.lifecycle.LiveData
import com.juri.kolo_android.data.local.entities.DbFamily
import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.data.local.entities.DbUser
import com.juri.kolo_android.data.model.AuthResponse
import com.juri.kolo_android.data.model.CreateFamilyBody
import com.juri.kolo_android.data.model.DepositBody
import com.juri.kolo_android.data.model.DepositResponse
import com.juri.kolo_android.data.model.FamilyResponse
import com.juri.kolo_android.data.model.JoinFamilyBody
import com.juri.kolo_android.data.model.LoginBody
import com.juri.kolo_android.data.model.RegisterBody
import com.juri.kolo_android.data.model.TransactionResponse
import com.juri.kolo_android.data.model.WithdrawBody
import com.juri.kolo_android.data.model.WithdrawResponse
import com.juri.kolo_android.utils.NetworkResult

interface Repository {

    val user: LiveData<DbUser>

    val txns: LiveData<List<DbTransactions>>

    val family: LiveData<DbFamily>

    suspend fun loginUser(loginBody: LoginBody): NetworkResult<AuthResponse>

    suspend fun registerUser(registerBody: RegisterBody): NetworkResult<AuthResponse>

    suspend fun createFamily(createFamilyBody: CreateFamilyBody, userId: Int): NetworkResult<FamilyResponse>

    suspend fun joinFamily(joinFamilyBody: JoinFamilyBody, userId: Int): NetworkResult<FamilyResponse>

    suspend fun deposit(depositBody: DepositBody): NetworkResult<DepositResponse>

    suspend fun withdraw(withdrawBody: WithdrawBody): NetworkResult<WithdrawResponse>

    suspend fun fetchTransactions(familyId: Int): NetworkResult<TransactionResponse>

    suspend fun logout()
}