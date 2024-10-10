package com.juri.kolo_android.data.remote

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface KoloService {

    @POST("/users/register")
    suspend fun registerUser(@Body registerBody: RegisterBody): Response<AuthResponse>

    @POST("/users/login")
    suspend fun loginUser(@Body loginBody: LoginBody): Response<AuthResponse>

    @POST("transactions/deposit")
    suspend fun deposit(@Body depositBody: DepositBody): Response<DepositResponse>

    @POST("transactions/withdraw")
    suspend fun withdraw(@Body withdrawBody: WithdrawBody): Response<WithdrawResponse>

    @GET("transactions/{familyId}")
    suspend fun fetchTxns(
        @Path(
            "familyId",
            encoded = true
        ) familyId: Int
    ): Response<TransactionResponse>

    @GET("transactions/verify")
    suspend fun verifyTxn(@Query("reference") ref: String)

    @POST("family/create/{userId}")
    suspend fun createFamily(
        @Body createFamilyBody: CreateFamilyBody,
        @Path("userId") userId: Int
    ): Response<FamilyResponse>

    @POST("/family/join/{userId}")
    suspend fun joinFamily(
        @Body joinFamilyBody: JoinFamilyBody,
        @Path("userId") userId: Int
    ): Response<FamilyResponse>
}