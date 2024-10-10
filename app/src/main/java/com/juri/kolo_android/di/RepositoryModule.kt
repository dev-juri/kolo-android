package com.juri.kolo_android.di

import com.juri.kolo_android.data.local.KoloDao
import com.juri.kolo_android.data.remote.KoloService
import com.juri.kolo_android.data.repository.Repository
import com.juri.kolo_android.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepo(
        db: KoloDao,
        service: KoloService,
        dispatcher: CoroutineDispatcher
    ): Repository = RepositoryImpl(db, service, dispatcher) as Repository
}