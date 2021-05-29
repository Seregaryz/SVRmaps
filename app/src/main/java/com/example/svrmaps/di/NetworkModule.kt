package com.example.svrmaps.di

import android.content.Context
import android.os.Build
import com.example.svrmaps.BuildConfig
import com.example.svrmaps.network.api.Api
import com.example.svrmaps.utils.Tls12SocketFactory.Companion.enableTls12
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import lc.deck.rudn.BuildConfig
import lc.deck.rudn.data.cache.EventCalendarCache
import lc.deck.rudn.data.server.Api
import lc.deck.rudn.data.server.ApiWithEventCalendarCache
import lc.deck.rudn.data.server.interceptor.AuthHeaderInterceptor
import lc.deck.rudn.data.server.interceptor.ErrorResponseInterceptor
import lc.deck.rudn.system.LanguageManager
import lc.deck.rudn.system.LocaleHolder
import lc.deck.rudn.system.SessionKeeper
import lc.deck.rudn.utils.Tls12SocketFactory.Companion.enableTls12
import lc.deck.rudn.utils.setSslContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
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

    @Provides
    @Singleton
    fun provideApi(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Api {
        return with(Retrofit.Builder()) {
            addConverterFactory(GsonConverterFactory.create(gson))
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(okHttpClient)
            baseUrl(BuildConfig.BASE_URL)
            build()
        }.create(Api::class.java)
    }
}