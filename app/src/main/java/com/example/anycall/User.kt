package com.example.anycall

import android.net.Uri

object User {
    private val user = MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_dummy"), "보노보노", R.drawable.ic_star_blank,"jeong223@hanmail.net", "월요병 지옥이야", "01020334856")
    fun getUser(): MyItem = user

    fun updateUserMessage(message: String) {
        user.myMessage = message
    }

}