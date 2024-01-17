package com.example.anycall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.anycall.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabTextList = listOf("CONTACT","MY PAGE")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewPager()
    }

    private fun initViewPager() {
        binding.viewpager2.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewpager2) {tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }

}

/**
 * 데이터 넣기, item.xml 구성하기, 롱클릭으로 삭제, 플로팅버튼 추가
 */