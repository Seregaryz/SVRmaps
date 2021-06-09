package com.example.svrmaps.model.user

import com.google.gson.annotations.SerializedName

data class UserAccount(

    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("token")
    val token: String?
)