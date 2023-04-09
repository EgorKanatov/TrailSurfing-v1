package com.example.trailsurfing.data

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Point(
    val lat: Double? = null,
    val lon: Double? = null
) : Parcelable {}
