package com.example.anycall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anycall.MyItem.Companion.dataList
import com.example.anycall.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.button1.setOnClickListener {
            setFragment(ContactsFragment())
        }

        setFragment(ContactsFragment())

    }


    private fun setFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, frag)
            setReorderingAllowed(true)
            addToBackStack("")
        }.commit()
    }
}

/**
 * 데이터 넣기, item.xml 구성하기, 롱클릭으로 삭제, 플로팅버튼 추가
 */