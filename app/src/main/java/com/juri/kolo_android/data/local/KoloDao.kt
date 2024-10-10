package com.juri.kolo_android.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.data.local.entities.DbUser

@Dao
interface KoloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg user: DbUser)

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): LiveData<DbUser>

    @Query("SELECT * FROM User WHERE id=:userId")
    suspend fun getCurrUser(userId: Int): DbUser

    @Query("DELETE FROM User")
    suspend fun deleteUser()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(vararg transaction: DbTransactions)

    @Query("SELECT * FROM Transactions ORDER BY `update` DESC")
    fun getTransactions(): LiveData<List<DbTransactions>>

    @Query("DELETE FROM Transactions")
    suspend fun deleteTransactions()
}