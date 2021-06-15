package com.example.svrmaps.model.subject

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Subject(
    val name: String,
    val description: String,
    val latitude: Double?,
    val longitude: Double?
) : Parcelable
