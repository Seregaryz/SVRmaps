package com.example.svrmaps.di

import com.example.svrmaps.network.repository.SubjectRepository
import com.example.svrmaps.network.repository.SubjectRepositoryImpl
import com.example.svrmaps.network.repository.UserRepository
import com.example.svrmaps.network.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSubjectRepository(subjectRepository: SubjectRepositoryImpl): SubjectRepository =
        subjectRepository

    @Provides
    @Singleton
    fun provideUserRepository(userRepository: UserRepositoryImpl): UserRepository =
        userRepository

}