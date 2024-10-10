package com.juri.kolo_android.data.mapper

import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.data.model.TransactionData

fun TransactionData.toDbModel(): Array<DbTransactions> {
    return transactions.map {
        DbTransactions(
            id = it.id,
            type = it.type,
            status = it.status,
            amount = it.amount.toDouble(),
            transactionRef = it.transactionRef,
            accessCode = it.accessCode,
            created = it.created,
            update = it.update,
            accountName = it.accountName,
            bankName = it.bankName,
            accountNumber = it.accountNumber,
            remarks = it.remarks
        )
    }.toTypedArray()
}