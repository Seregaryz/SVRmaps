package com.example.svrmaps.di

import android.content.Context
import android.os.Build
import com.example.svrmaps.BuildConfig
import com.example.svrmaps.system.Tls12SocketFactory.Companion.enableTls12
import com.example.svrmaps.utils.setSslContext
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClientBuilder(
        @ApplicationContext context: Context
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            enableTls12()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                setSslContext(context.resources)
            }
            if (BuildConfig.DEBUG) {
                val httpLogger = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addNetworkInterceptor(httpLogger)
            }
        }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        okHttpClientBuilder: OkHttpClient.Builder,
        gson: Gson
    ): OkHttpClient =
        with(okHttpClientBuilder) {
            if (BuildConfig.DEBUG) {
                val httpLogger = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addNetworkInterceptor(httpLogger)
            }
            build()
        }
}