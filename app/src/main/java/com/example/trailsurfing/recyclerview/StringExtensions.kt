package com.example.trailsurfing.recyclerview

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
fun String.toInstant(): Instant = Instant.parse(this)