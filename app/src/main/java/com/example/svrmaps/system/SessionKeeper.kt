package com.example.svrmaps.system

import android.webkit.CookieManager
import com.example.svrmaps.data.storage.Prefs
import com.example.svrmaps.model.user.UserAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionKeeper @Inject constructor(private val prefs: Prefs) {

    var userAccount: UserAccount? = prefs.account
        private set

    val token: String?
        get() = userAccount?.token

    fun setUserAccount(userAccount: UserAccount?) {
        this.userAccount = userAccount
        prefs.account = userAccount
        if (userAccount == null) {
            @Suppress("DEPRECATION")
            CookieManager.getInstance().removeAllCookie()
        }
    }

}