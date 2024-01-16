package com.example.anycall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anycall.databinding.FragmentContactDetailBinding

private const val USER_IMAGE = "user_image"
private const val USER_PHONE = "user_phone"
private const val USER_MESSAGE = "user_message"
private const val USER_EMAIL = "user_email"

class ContactDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null
    private val binding by lazy { FragmentContactDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(USER_IMAGE)
            param2 = it.getString(USER_PHONE)
            param3 = it.getString(USER_MESSAGE)
            param4 = it.getString(USER_EMAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String, param4: String) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_IMAGE, param1)
                    putString(USER_PHONE, param2)
                    putString(USER_MESSAGE, param3)
                    putString(USER_EMAIL, param4)
                }
            }
    }
}