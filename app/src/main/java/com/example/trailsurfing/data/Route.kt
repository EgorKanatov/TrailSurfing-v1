package com.example.trailsurfing.data

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import com.example.trailsurfing.data.Point

@IgnoreExtraProperties
@Parcelize
//класс даных, получаемых из базы данных
data class Route(
    val title: String? = null,
    val description: String? = null,
    var imageUrl: String? = null,
    var pathUUID: MutableList<Point> = mutableListOf(),
    val uuid: String? = null,
    val saved: Boolean? = false,

    ) : Parcelable {}