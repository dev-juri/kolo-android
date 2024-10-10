package com.juri.kolo_android.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class DbUser(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    var balance: Double,
    val created: String,
    val updated: String
)

@Entity(tableName = "Transactions")
data class DbTransactions(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val type: String,
    val status: String,
    val amount: Double,
    val transactionRef: String,
    val accessCode: String,
    val created: String,
    val update: String,
    val accountName: String?,
    val accountNumber: String?,
    val remarks: String?
)