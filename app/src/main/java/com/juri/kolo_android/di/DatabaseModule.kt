package com.juri.kolo_android.di

import android.app.Application
import androidx.room.Room
import com.juri.kolo_android.data.local.KoloDao
import com.juri.kolo_android.data.local.KoloDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): KoloDatabase {
        return Room.databaseBuilder(application, KoloDatabase::class.java, "Kolo.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideClassboardDao(database: KoloDatabase): KoloDao {
        return database.koloDao
    }

}