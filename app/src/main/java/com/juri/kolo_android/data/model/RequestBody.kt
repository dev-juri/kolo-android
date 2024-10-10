package com.juri.kolo_android.data.model

import com.squareup.moshi.Json

data class RegisterBody(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)

data class AuthResponse(
    val statusCode: Int,
    val message: String = "",
    val data: UserData
)

data class UserData(
    val id: Int,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val created: String,
    val updated: String
)

data class LoginBody(
    val email: String,
    val password: String
)

data class DepositBody(
    val userId: Int,
    val familyId: Int,
    val amount: Double
)

data class DepositResponse(
    val statusCode: Int,
    val message: String,
    val data: DepositResponseData
)

data class DepositResponseData(
    @Json(name = "authorization_url")
    val authorizationUrl: String
)

data class GeneralResponse(
    val statusCode: Int,
    val message: String = "",
    val error: String = ""
)

data class WithdrawBody(
    val userId: Int,
    val accountName: String,
    val accountNumber: String,
    val bankName: String,
    val remarks: String?,
    val amount: Double,
    val familyId: Int
)

data class WithdrawResponse(
    val statusCode: Int,
    val message: String,
    val data: WithdrawalData
)

data class WithdrawalData(
    val familyId: Int,
    val balance: String,
    val transaction: Transaction
)

data class TransactionResponse(
    val statusCode: Int,
    val message: String,
    val data: TransactionData
)

data class TransactionData(
    val familyId: Int,
    val balance: Double,
    val transactions: List<Transaction>
)

data class Transaction(
    val id: Int,
    val type: String,
    val status: String,
    val amount: String,
    @Json(name = "transaction_ref")
    val transactionRef: String,
    @Json(name = "access_code")
    val accessCode: String,
    val created: String,
    val update: String,
    val accountName: String?,
    val bankName: String?,
    val accountNumber: String?,
    val remarks: String?
)

data class CreateFamilyBody(
    val name: String
)

data class JoinFamilyBody(
    val familyCode: String
)

data class FamilyResponse(
    val message: String,
    val data: FamilyData,
    val statusCode: Int
)

data class FamilyData(
    val family: Family
)

data class Family(
    val id: Int,
    val familyCode: String,
    val name: String,
    val balance: String,
    val ownerId: Int
)
