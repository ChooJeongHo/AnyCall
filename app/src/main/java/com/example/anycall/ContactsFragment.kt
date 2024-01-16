package com.example.anycall

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.anycall.MyItem.Companion.dataList
import com.example.anycall.databinding.FragmentContactsBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ContactsFragment : Fragment() {
    private var isImageSelected = false
    private var param1: String? = null
    private var param2: String? = null
    private val binding by lazy { FragmentContactsBinding.inflate(layoutInflater) }
    private val DEFAULT_GALLERY_REQUEST_CODE = 123
    private lateinit var selectedImageUri: Uri
    private lateinit var userImg: ImageView

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
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MyAdapter(MyItem.dataList)
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            floatingBtn.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("커스텀 다이얼로그")
                builder.setIcon(R.mipmap.ic_launcher)

                val v1 = layoutInflater.inflate(R.layout.add_user_dialog, null)
                builder.setView(v1)

                userImg = v1.findViewById(R.id.addUserImg)
                val nameEdit = v1.findViewById<EditText>(R.id.addUserName)
                val phoneEdit = v1.findViewById<EditText>(R.id.addUserPhone)
                val statusEdit = v1.findViewById<EditText>(R.id.addUserStatus)
                val emailEdit = v1.findViewById<EditText>(R.id.addUserEmail)
                userImg.setOnClickListener {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(intent, DEFAULT_GALLERY_REQUEST_CODE)
                }
                val listener = DialogInterface.OnClickListener { p0, p1 ->
                    val name = nameEdit.text.toString()
                    val phone = phoneEdit.text.toString()
                    val state = statusEdit.text.toString()
                    val email = emailEdit.text.toString()

                    val newItem: MyItem
                    if(isImageSelected){
                        newItem = MyItem(
                                selectedImageUri,
                                name,
                                R.drawable.ic_star_blank,
                                email,
                                state,
                                phone
                            )
                        isImageSelected = !isImageSelected
                    } else{
                        newItem = MyItem(
                            Uri.parse("android.resource://com.example.anycall/drawable/user"),
                            name,
                            R.drawable.ic_star_blank,
                            email,
                            state,
                            phone
                        )
                    }
                    dataList.add(newItem)


                    adapter.notifyDataSetChanged()
                }

                builder.setPositiveButton("확인", listener)
                builder.setNegativeButton("취소", null)

                builder.show()

            }

        }

        adapter.itemClick = object : MyAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame, ContactDetailFragment.newInstance(dataList[position]))
                    setReorderingAllowed(true)
                    addToBackStack("")
                }.commit()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            DEFAULT_GALLERY_REQUEST_CODE -> {
                data ?: return
                selectedImageUri = data.data as Uri
                Glide.with(this).load(selectedImageUri).into(userImg)
                isImageSelected = !isImageSelected
            }

            else -> {
                userImg.setImageResource(R.drawable.user)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}