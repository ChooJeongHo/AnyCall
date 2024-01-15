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
        dataList.add(MyItem(R.drawable.ic_launcher_foreground, "asdf", R.drawable.ic_launcher_background))
        dataList.add(MyItem(R.drawable.ic_launcher_foreground, "qwer", R.drawable.ic_launcher_background))

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