package com.example.anycall

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyItem(
    val icon:Int,
    val name:String,
    val like:Int
): Parcelable
