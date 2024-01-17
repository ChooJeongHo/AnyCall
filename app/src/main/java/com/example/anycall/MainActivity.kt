package com.example.anycall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anycall.MyItem.Companion.dataList
import com.example.anycall.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabTextList = listOf("CONTACT","MY PAGE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewPager()
        loadUserData()
    }

    private fun initViewPager() {
        binding.viewpager2.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewpager2) {tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }

    private fun loadUserData() {
        val pref = getSharedPreferences("pref",0)
        val message = pref.getString("message","") ?: ""
        User.updateUserMessage(message)
    }

    private fun saveUserMessage(message: String) {
        val pref = getSharedPreferences("pref",0)
        val edit = pref.edit()
        edit.putString("message", message).apply()
    }

    override fun onStop() {
        saveUserMessage(User.getUser().myMessage)
        super.onStop()
    }

}