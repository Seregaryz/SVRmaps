package com.example.svrmaps.model.exchange

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exchange(
    val offerSubjectName: String,
    val offerSubjectDescription: String,
    val offerLatitude: Double?,
    val offerLongitude: Double?,
    val offerUserEmail: String?,
    val destSubjectName: String,
    val destSubjectDescription: String,
    val destLatitude: Double?,
    val destLongitude: Double?,
    val destUserEmail: String?,
    val isClosed: Boolean = false
) : Parcelable
