package com.example.anycall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.anycall.databinding.FragmentMyPageBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyPageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val binding by lazy { FragmentMyPageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewpager()

        return binding.root
    }

    private fun initViewpager() {
        val favoriteAdapter = FavoriteAdapter(MyItem.dataList.filter { it.favorite }.toMutableList())
        binding.mypageViewpager.apply {
            adapter = favoriteAdapter
            isUserInputEnabled = false
        }
        binding.icArrowBack.setOnClickListener {
            val current = binding.mypageViewpager.currentItem

            binding.mypageViewpager.setCurrentItem(current - 1, true)
        }
        binding.icArrowForward.setOnClickListener {
            val current = binding.mypageViewpager.currentItem

            binding.mypageViewpager.setCurrentItem(current + 1, true)

            if (current == favoriteAdapter.itemCount - 1) {
                binding.mypageViewpager.setCurrentItem(0, true)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}