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
    val bankName: String?,
    val accountNumber: String?,
    val remarks: String?
)

@Entity(tableName = "Family")
data class DbFamily(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val familyCode: String,
    val ownerId: Int,
    val name: String,
    var balance: Double,
)

