package com.example.svrmaps.data.storage

import android.content.Context
import com.example.svrmaps.model.user.UserAccount
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private fun getSharedPreferences(prefsName: String) =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    private val AUTH_DATA = "auth_data"
    private val KEY_USER_ACCOUNT = "ad_accounts"
    private val authPrefs by lazy { getSharedPreferences(AUTH_DATA) }

    private val accountsTypeToken = object : TypeToken<UserAccount>() {}.type
    var account: UserAccount?
        get() = authPrefs
            .getString(KEY_USER_ACCOUNT, null)
            ?.let { gson.fromJson<UserAccount>(it, accountsTypeToken) }
        set(value) {
            if (value != null) {
                authPrefs.edit().putString(KEY_USER_ACCOUNT, gson.toJson(value)).apply()
            } else {
                authPrefs.edit().remove(KEY_USER_ACCOUNT).apply()
            }
        }


    //app settings
    private val SETTINGS_DATA = "settings_data"
    private val settingsPrefs by lazy { getSharedPreferences(SETTINGS_DATA) }

    fun getNotificationEnabled(id: Int): Boolean = settingsPrefs.getBoolean(id.toString(), true)

    fun setNotificationEnabled(id: Int, enabled: Boolean) {
        settingsPrefs.edit().putBoolean(id.toString(), enabled).apply()
    }

}