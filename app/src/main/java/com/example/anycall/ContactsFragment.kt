package com.example.anycall

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
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
    private lateinit var adapter: MyAdapter
    lateinit var requestLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val status = ContextCompat.checkSelfPermission(requireContext(), "android.permission.READ_CONTACTS")
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>("android.permission.READ_CONTACTS"), 100)
            Log.d("test", "permission denied")
        }
        requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                //주소록정보 가져오기
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d("test", "permission granted")
            val contacts = getContacts()
            dataList.addAll(contacts)
            adapter.notifyDataSetChanged()
        } else{
            Log.d("test", "permission denied")
        }
    }
    private fun getContacts(): ArrayList<MyItem> {
        val contacts = ArrayList<MyItem>()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )

        val cursor = requireContext().contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val photoUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
            val hasPhoneNumberIndex = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

            while (it.moveToNext()) {
                val contactId = it.getString(idIndex)
                val name = it.getString(nameIndex)

                val photoUriString = if (photoUriIndex != -1) it.getString(photoUriIndex) else null
                val photoUri = if (!photoUriString.isNullOrBlank()) Uri.parse(photoUriString) else null

                val hasPhoneNumber = it.getInt(hasPhoneNumberIndex) > 0

                val phoneNumbers = if (hasPhoneNumber) getContactNumbers(contactId) else emptyList()

                val contact = MyItem(photoUri, name, R.drawable.ic_star_blank, "", "", phoneNumbers.firstOrNull() ?: "")
                contacts.add(contact)
            }
        }

        return contacts
    }

    private fun getContactNumbers(contactId: String): List<String> {
        val numbers = ArrayList<String>()

        val cursor = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId),
            null
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                // Check if NUMBER exists in the cursor
                val phoneNumber = if (numberIndex != -1) it.getString(numberIndex) else null
                phoneNumber?.let { number -> numbers.add(number) }
            }
        }

        return numbers
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contacts, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list_view -> {
                // 메뉴 아이템이 클릭되었을 때의 동작을 여기에 작성합니다.
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                adapter.setGridView(false)
                binding.recyclerView.adapter = adapter
                true
            }
            R.id.action_grid_view -> {
                // 메뉴 아이템이 클릭되었을 때의 동작을 여기에 작성합니다.
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
                adapter.setGridView(true)
                binding.recyclerView.adapter = adapter
                true
            }
            else -> super.onOptionsItemSelected(item)
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

        adapter = MyAdapter(MyItem.dataList)
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