package com.example.anycall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.anycall.databinding.FragmentContactDetailBinding

class ContactDetailFragment : Fragment() {

    private val binding by lazy { FragmentContactDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receiveData = arguments?.getParcelable<MyItem>("EXTRA_USER")

        binding.userImage.setImageURI(receiveData?.icon)
        binding.userName.text = receiveData?.name
        binding.userPhone.text = receiveData?.phoneNum
        binding.userMessage.text = receiveData?.myMessage
        binding.userEmail.text = receiveData?.email

        binding.btnMessage.setOnClickListener {
            val message = receiveData?.phoneNum
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("smsto:" + message)
            startActivity(intent)
        }

        binding.btnCall.setOnClickListener {
            val dial = receiveData?.phoneNum
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + dial)
            startActivity(intent)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(myItem: MyItem) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("EXTRA_USER", myItem)
                }
            }
    }
}