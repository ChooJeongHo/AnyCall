package com.example.anycall

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyItem(
    val icon: Uri?,
    val name: String,
    val like: Int,
    val email: String,
    val myMessage:String,
    val phoneNum:String
) : Parcelable {
    companion object {
        val dataList = mutableListOf(
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person1"), "Sophia", R.drawable.ic_star_blank, "asdf123@naver.com", "안녕하세요","01012341234"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person2"), "Emma", R.drawable.ic_star_blank,"qwer123@gmail.com", "혼자살아요", "01012345678"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person3"), "Olivia", R.drawable.ic_star_blank, "zxcv123@naver.com", "개발자하고싶다", "01052684568"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person4"), "Isabella", R.drawable.ic_star_blank, "dkdk1212@gmail.com", "나 오늘 취하고싶다", "01065894525"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person5"), "Ava", R.drawable.ic_star_blank,"kim2994@naver.com", "심심합니다", "01032343020"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person6"), "Mia", R.drawable.ic_star_blank,"jwidn2994@naver.com", "심심하면 연락주세요", "01069430684"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person7"), "Emily", R.drawable.ic_star_blank,"tibsjd322@naver.com", "컴퓨터 추천해주세요", "01059374323"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person8"), "Abigail", R.drawable.ic_star_blank,"bonobono290@naver.com", "보노보노에효", "01024919248"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person9"), "Madison", R.drawable.ic_star_blank,"dkgkrltlfxk201@gmail.com", "아 일하기 실타", "01082103920"),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/ic_person10"), "Elizabeth", R.drawable.ic_star_blank,"djeltjwlfkfdldi@hanmail.net", "월요병 지옥이야", "01049482838")
        )
    }
}
