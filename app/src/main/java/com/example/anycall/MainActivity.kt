package com.example.anycall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anycall.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dataList = mutableListOf<MyItem>()
        dataList.add(MyItem(R.drawable.ic_person1, "Sophia", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person2, "Emma", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person3, "Olivia", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person4, "Isabella", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person5, "Ava", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person6, "Mia", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person7, "Emily", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person8, "Abigail", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person9, "Madison", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person10, "Elizabeth", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person11, "Charlotte", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person12, "Avery", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person13, "Sofia", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person14, "Chloe", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person15, "Ella", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person16, "Harper", R.drawable.ic_star_blank))
        dataList.add(MyItem(R.drawable.ic_person17, "Amelia", R.drawable.ic_star_blank))


        val adapter = MyAdapter(dataList)
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
 * 데이터 넣기, item.xml 구성하기, 롱클릭으로 삭제, 플로팅버튼 추가
 */