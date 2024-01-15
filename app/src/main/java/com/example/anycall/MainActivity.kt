package com.example.anycall

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anycall.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val adapter = MyAdapter(MyItem.dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.button1.setOnClickListener {
            setFragment(ContactsFragment())
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
 * 데이터 넣기, item.xml 구성하기, 롱클릭으로 학제, 플로팅버튼 추가
 */