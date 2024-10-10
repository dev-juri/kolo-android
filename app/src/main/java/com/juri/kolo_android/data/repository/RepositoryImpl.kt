package com.juri.kolo_android.data.repository

import androidx.lifecycle.LiveData
import com.juri.kolo_android.data.local.KoloDao
import com.juri.kolo_android.data.local.entities.DbFamily
import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.data.local.entities.DbUser
import com.juri.kolo_android.data.mapper.toDbModel
import com.juri.kolo_android.data.model.AuthResponse
import com.juri.kolo_android.data.model.CreateFamilyBody
import com.juri.kolo_android.data.model.DepositBody
import com.juri.kolo_android.data.model.DepositResponse
import com.juri.kolo_android.data.model.FamilyResponse
import com.juri.kolo_android.data.model.GeneralResponse
import com.juri.kolo_android.data.model.JoinFamilyBody
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

    override val user: LiveData<DbUser> = db.getUser()

    override val txns: LiveData<List<DbTransactions>> = db.getTransactions()

    override val family: LiveData<DbFamily> = db.fetchFamily()

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
                NetworkResult.Error("$e Something went wrong, please check your internet connection and try again later.")
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
                NetworkResult.Error("$e Something went wrong, please check your internet connection and try again later.")
            }
        }

    override suspend fun createFamily(createFamilyBody: CreateFamilyBody, userId: Int): NetworkResult<FamilyResponse> =
        withContext(dispatcher) {
            return@withContext try {
                val response = service.createFamily(createFamilyBody, userId)
                if (response.isSuccessful) {
                    val body = response.body()!!

                    val newFam = DbFamily(
                        body.data.family.id,
                        body.data.family.familyCode,
                        body.data.family.ownerId,
                        body.data.family.name,
                        body.data.family.balance.toDouble()
                    )
                    db.deleteFamily()
                    db.insertFamily(newFam)
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

    override suspend fun joinFamily(joinFamilyBody: JoinFamilyBody, userId: Int): NetworkResult<FamilyResponse> =
        withContext(dispatcher) {
            return@withContext try {
                val response = service.joinFamily(joinFamilyBody, userId)
                if (response.isSuccessful) {
                    val body = response.body()!!

                    val newFam = DbFamily(
                        body.data.family.id,
                        body.data.family.familyCode,
                        body.data.family.ownerId,
                        body.data.family.name,
                        body.data.family.balance.toDouble()
                    )
                    db.deleteFamily()
                    db.insertFamily(newFam)
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
                        txn.bankName,
                        txn.accountNumber,
                        txn.remarks
                    )

                    val family = db.getCurrFamily(body.familyId)
                    family.balance = body.balance.toDouble()

                    db.insertFamily(family)
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

    override suspend fun fetchTransactions(familyId: Int): NetworkResult<TransactionResponse> =
        withContext(dispatcher) {
            try {
                val response = service.fetchTxns(familyId)

                return@withContext if (response.isSuccessful) {
                    val body = response.body()!!

                    val family = db.getCurrFamily(familyId)
                    family.balance = body.data.balance

                    db.insertFamily(family)
                    db.deleteTransactions()
                    db.insertTransaction(*body.data.toDbModel())
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
}