package com.example.anycall

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyItem(
    val icon: Uri?,
    val name: String,
    val email: String,
    var myMessage:String,
    val phoneNum:String,
    var favorite: Boolean = false,
    var isSwiped: Boolean = false
) : Parcelable {
    companion object {
        val dataList = mutableListOf(
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img1"), "고윤정",  "asdf123@naver.com", "무빙 고윤정","01012341234", true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img2"), "한지현", "qwer123@gmail.com", "펜트하우스 한지현", "01012345678",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img3"), "송중기", "zxcv123@naver.com", "태양의후예 송중기", "01052684568",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img4"), "한소희",  "dkdk1212@gmail.com", "경성크리처 한소희", "01065894525",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img5"), "해린", "kim2994@naver.com", "뉴진스 해린", "01032343020",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img6"), "최우식","jwidn2994@naver.com", "그 해 우리는 최우식", "01069430684", true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img7"), "박보영", "tibsjd322@naver.com", "콘크리트 유토피아 박보영", "01059374323",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img8"), "이동욱", "bonobono290@naver.com", "구미호뎐 이동욱", "01024919248",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img9"), "김태리", "dkgkrltlfxk201@gmail.com", "악귀 김태리", "01082103920",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img10"), "미연", "djeltjwlfkfdldi@hanmail.net", "아이들 미연", "01049482838",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img11"), "차은우", "GoGo123@hanmail.net", "아스트로 차은우", "01033950023",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img12"), "안유진", "han4432@naver.com", "아이브 안유진", "01084750394",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img13"), "아이유", "song113@hanmail.net", "그냥 아이유", "01059395027",true),
            MyItem(Uri.parse("android.resource://com.example.anycall/drawable/contact_img14"), "이도현","han8831@naver.com", "더글로리 이도현", "01033505836",true)
        )
        fun clickFavorite(myItem: MyItem): Boolean {
            val index = dataList.indexOf(myItem)
            dataList[index].favorite = !dataList[index].favorite
            return dataList[index].favorite
        }
    }


}
