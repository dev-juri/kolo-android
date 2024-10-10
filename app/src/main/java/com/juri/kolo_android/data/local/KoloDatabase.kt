package com.juri.kolo_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juri.kolo_android.data.local.entities.DbFamily
import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.data.local.entities.DbUser

@Database(
    entities = [DbUser::class, DbTransactions::class, DbFamily::class],
    version = 1,
    exportSchema = false
)
abstract class KoloDatabase : RoomDatabase() {

    abstract val koloDao: KoloDao
}