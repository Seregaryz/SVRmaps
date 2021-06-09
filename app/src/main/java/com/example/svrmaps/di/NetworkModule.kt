package com.example.svrmaps.di

import android.content.Context
import android.os.Build
import com.example.svrmaps.BuildConfig
import com.example.svrmaps.R
import com.example.svrmaps.network.api.Api
import com.example.svrmaps.system.Tls12SocketFactory.Companion.enableTls12
import com.example.svrmaps.utils.setSslContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(R.string.default_web_client_id.toString())
            .requestEmail()
            .build()

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context, gso: GoogleSignInOptions): GoogleSignInClient =
        GoogleSignIn.getClient(context, gso)

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
}