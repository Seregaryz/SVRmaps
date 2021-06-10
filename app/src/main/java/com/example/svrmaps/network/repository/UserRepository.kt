package com.example.svrmaps.network.repository

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private var auth: FirebaseAuth,
    private var googleSignInClient: GoogleSignInClient
) {

    fun signIn(email: String, password: String): String {
        //authenticate user
        var res = true
        var userId = ""
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithEmail:success ${auth.currentUser?.uid}")
                    userId = auth.currentUser?.uid ?: "not found"
                    res = true
                } else {
                    res = false
                    Timber.w("signInWithEmail:failure ${task.exception}")
                }
            }
        return userId
    }

    fun signInWithGoogle(): Boolean {
        var res = false
        val signInIntent = googleSignInClient.signInIntent
        val account = GoogleSignIn.getSignedInAccountFromIntent(signInIntent).result
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    res = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //Snackbar.make(binding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
        return res
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: "error"
    }


    fun createAccount(email: String, password: String): String {
        var res = true
        var userId = ""
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.tag(ContentValues.TAG).d("createUserWithEmail:success ${auth.currentUser?.uid}")
                    userId = auth.currentUser?.uid ?: "not found"
                    res = true
                } else {
                    // If sign in fails, display a message to the user.
                    res = false
                    Timber.tag(ContentValues.TAG).w(task.exception, "createUserWithEmail:failure")
                }
            }
        return userId
    }

    companion object {
        private const val TAG = "Authorization"
    }
}