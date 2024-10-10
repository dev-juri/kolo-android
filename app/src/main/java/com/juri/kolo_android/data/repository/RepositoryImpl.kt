package com.juri.kolo_android.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.juri.kolo_android.data.local.KoloDao
import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.data.local.entities.DbUser
import com.juri.kolo_android.data.mapper.toDbModel
import com.juri.kolo_android.data.model.AuthResponse
import com.juri.kolo_android.data.model.DepositBody
import com.juri.kolo_android.data.model.DepositResponse
import com.juri.kolo_android.data.model.GeneralResponse
import com.juri.kolo_android.data.model.LoginBody
import com.juri.kolo_android.data.model.RegisterBody
import com.juri.kolo_android.data.model.TransactionResponse
import com.juri.kolo_android.data.model.WithdrawBody
import com.juri.kolo_android.data.model.WithdrawResponse
import com.juri.kolo_android.data.remote.KoloService
import com.juri.kolo_android.utils.NetworkResult
import com.juri.kolo_android.utils.parseError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val db: KoloDao,
    private val service: KoloService,
    private val dispatcher: CoroutineDispatcher
) : Repository {

    override val user : LiveData<DbUser> = db.getUser()

    override val txns: LiveData<List<DbTransactions>> = db.getTransactions()

    override suspend fun loginUser(loginBody: LoginBody): NetworkResult<AuthResponse> =
        withContext(dispatcher) {
            try {
                val response = service.loginUser(loginBody)

                return@withContext if (response.isSuccessful) {
                    val body = response.body()
                    val user = body!!.data
                    val newDbUser = DbUser(
                        user.id,
                        user.fullName,
                        user.email,
                        user.phoneNumber,
                        user.balance,
                        user.created,
                        user.updated
                    )
                    db.deleteUser()
                    db.insertUser(newDbUser)
                    NetworkResult.Success(body)
                } else {
                    val error = parseError(
                        GeneralResponse::class.java, response.errorBody()!!
                    ) as GeneralResponse
                    NetworkResult.Error(error.message)
                }
            } catch (e: UnknownHostException) {
                NetworkResult.Error("Please check your internet connection and try again later.")
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please check your internet connection and try again later.")
            }
        }

    override suspend fun registerUser(registerBody: RegisterBody): NetworkResult<AuthResponse> =
        withContext(dispatcher) {
            try {
                val response = service.registerUser(registerBody)

                return@withContext if (response.isSuccessful) {
                    val body = response.body()
                    val user = body!!.data
                    val newDbUser = DbUser(
                        user.id,
                        user.fullName,
                        user.email,
                        user.phoneNumber,
                        user.balance,
                        user.created,
                        user.updated
                    )
                    db.deleteUser()
                    db.insertUser(newDbUser)
                    NetworkResult.Success(body)
                } else {
                    val error = parseError(
                        GeneralResponse::class.java, response.errorBody()!!
                    ) as GeneralResponse
                    NetworkResult.Error(error.message)
                }
            } catch (e: UnknownHostException) {
                NetworkResult.Error("Please check your internet connection and try again later.")
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please check your internet connection and try again later.")
            }
        }

    override suspend fun deposit(depositBody: DepositBody): NetworkResult<DepositResponse> =
        withContext(dispatcher) {
            try {
                val response = service.deposit(depositBody)

                return@withContext if (response.isSuccessful) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    val error = parseError(
                        GeneralResponse::class.java, response.errorBody()!!
                    ) as GeneralResponse
                    NetworkResult.Error(error.message)
                }
            } catch (e: UnknownHostException) {
                NetworkResult.Error("Please check your internet connection and try again later.")
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please check your internet connection and try again later.")
            }
        }

    override suspend fun withdraw(withdrawBody: WithdrawBody): NetworkResult<WithdrawResponse> =
        withContext(dispatcher) {
            try {
                val response = service.withdraw(withdrawBody)
                return@withContext if (response.isSuccessful) {
                    val body = response.body()!!.data
                    val txn = body.transaction
                    val dbTxn = DbTransactions(
                        txn.id,
                        txn.type,
                        txn.status,
                        txn.amount.toDouble(),
                        txn.transactionRef,
                        txn.accessCode,
                        txn.created,
                        txn.update,
                        txn.accountName,
                        txn.accountNumber,
                        txn.remarks
                    )

                    val user = db.getCurrUser(withdrawBody.userId)
                    user.balance = body.balance.toDouble()

                    db.insertUser(user)
                    db.insertTransaction(dbTxn)
                    NetworkResult.Success(response.body()!!)
                } else {
                    val error = parseError(
                        GeneralResponse::class.java, response.errorBody()!!
                    ) as GeneralResponse
                    NetworkResult.Error(error.message)
                }
            } catch (e: UnknownHostException) {
                NetworkResult.Error("Please check your internet connection and try again later.")
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please check your internet connection and try again later.")
            }
        }

    override suspend fun fetchTransactions(userId: Int): NetworkResult<TransactionResponse> = withContext(dispatcher) {
        try {
            val response = service.fetchTxns(userId)

            return@withContext if (response.isSuccessful) {
                val body = response.body()!!

                val user = db.getCurrUser(userId)
                user.balance = body.data.balance

                db.insertUser(user)
                db.deleteTransactions()
                db.insertTransaction(*body.data.toDbModel())
                NetworkResult.Success(body)
            }else{
                val error = parseError(
                    GeneralResponse::class.java, response.errorBody()!!
                ) as GeneralResponse
                NetworkResult.Error(error.message)
            }
        } catch (e: UnknownHostException) {
            NetworkResult.Error("Please check your internet connection and try again later.")
        } catch (e: Exception) {
            NetworkResult.Error("Something went wrong, please check your internet connection and try again later.")
        }
    }
}