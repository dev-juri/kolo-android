package com.juri.kolo_android.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juri.kolo_android.data.local.entities.DbFamily
import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.data.local.entities.DbUser

@Dao
interface KoloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: DbUser)

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): LiveData<DbUser>

    @Query("DELETE FROM User")
    suspend fun deleteUser()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(vararg transaction: DbTransactions)

    @Query("SELECT * FROM Transactions ORDER BY `update` DESC")
    fun getTransactions(): LiveData<List<DbTransactions>>

    @Query("DELETE FROM Transactions")
    suspend fun deleteTransactions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamily(family: DbFamily)

    @Query("SELECT * FROM Family LIMIT 1")
     fun fetchFamily(): LiveData<DbFamily>

     @Query("DELETE FROM Family")
     suspend fun deleteFamily()

    @Query("SELECT * FROM Family WHERE id=:familyId")
    suspend fun getCurrFamily(familyId: Int): DbFamily
}