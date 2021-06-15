package com.example.svrmaps.network.repository

import android.content.ContentValues
import android.util.Log
import com.example.svrmaps.model.user.UserAccount
import com.example.svrmaps.system.SessionKeeper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber
import javax.inject.Inject

interface UserRepository {

    fun signIn(email: String, password: String): String

    fun openNewAccount(userAccount: UserAccount)

    fun signInWithGoogle(): Boolean

    fun signOut()

    fun getCurrentUserId(): String

    fun createAccount(email: String, password: String): String

}