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


        val adapter = MyAdapter(dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.button1.setOnClickListener {
            setFragment(ContactsFragment())
        }

        adapter.itemClick = object : MyAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val fragment = ContactDetailFragment.newInstance(dataList[position].icon, dataList[position].phoneNum, dataList[position].myMessage, dataList[position].email)
                setFragment(fragment)
            }
        }
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