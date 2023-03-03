package com.coryrdunn.pscodeexercise.di

import com.coryrdunn.pscodeexercise.data.repository.DataRepositoryImpl
import com.coryrdunn.pscodeexercise.domain.repository.DataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Binds
    @Singleton
    abstract fun dataRepository(dataRepository: DataRepositoryImpl): DataRepository
}