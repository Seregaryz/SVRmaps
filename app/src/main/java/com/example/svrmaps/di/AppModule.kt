package com.example.svrmaps.di

import android.content.Context
import com.example.predicate.model.schedulers.AppSchedulers
import com.example.predicate.model.schedulers.SchedulersProvider
import com.example.svrmaps.App
import com.example.svrmaps.interactor.UserInteractor
import com.example.svrmaps.system.ErrorHandler
import com.example.svrmaps.system.ResourceManager
import com.example.svrmaps.system.message.SystemMessageNotifier
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModule {

    companion object {

        @Provides
        @Singleton
        fun provideSchedulers(): SchedulersProvider = AppSchedulers()

        @Provides
        @Singleton
        fun provideGson(): Gson = with(GsonBuilder()) {
            serializeNulls()
            create()
        }

        @Provides
        @Singleton
        fun provideSystemMessageNotifier(): SystemMessageNotifier = SystemMessageNotifier()

        @Provides
        @Singleton
        fun providedErrorHandler(
            systemMessageNotifier: SystemMessageNotifier,
            schedulers: SchedulersProvider,
            resourceManager: ResourceManager,
            sessionInteractor: UserInteractor
        ): ErrorHandler =
            ErrorHandler(
                systemMessageNotifier,
                schedulers,
                resourceManager,
                sessionInteractor
            )

    }

}