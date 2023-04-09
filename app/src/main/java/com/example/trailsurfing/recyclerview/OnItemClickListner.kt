package com.example.trailsurfing.recyclerview

import android.view.View

interface OnItemClickListner<T> {
    fun onClick(view: View, item: T)
}