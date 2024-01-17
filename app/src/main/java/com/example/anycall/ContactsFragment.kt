package com.example.anycall

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.ContentUris
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
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
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Contacts"
        setHasOptionsMenu(true)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * 사용권한 여부 물어보기
     */
    private fun initPermission() {
        val status = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        )
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                100
            )
        } else {
            updateContactList()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // READ_CONTACTS 권한이 부여되면 연락처 업데이트를 진행
            updateContactList()
        }
    }

    /**
     * 전화번호부에서 데이터 가져오기
     */
    private fun updateContactList() {
        if (isDataLoaded) return

        isDataLoaded = true
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val nameColumnIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneNumberColumnIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
//                val contactIdColumnIndex =
//                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
                val contactIdColumnIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID)
                if (nameColumnIndex >= 0 && phoneNumberColumnIndex >= 0 && contactIdColumnIndex >= 0) {
                    val name = cursor.getString(nameColumnIndex)
                    val phoneNumber = cursor.getString(phoneNumberColumnIndex)
//                    val contactId = cursor.getLong(contactIdColumnIndex)
                    val rawContactId = cursor.getLong(contactIdColumnIndex)

                    // 여기서 필요한 데이터를 가져와서 MyItem 객체를 생성하여 dataList에 추가
                    val newItem = MyItem(
                        icon = getContactPhotoUri(rawContactId) ?:null,
                        name = name ?: "",
                        like = R.drawable.ic_star_blank,
                        email = getEmail(rawContactId) ?: "",
                        myMessage = "",
                        phoneNum = phoneNumber ?: ""
                    )

                    dataList.add(newItem)
                }
            }
            cursor.close()
        }

        if (!::adapter.isInitialized) {
            adapter = MyAdapter(dataList)
            binding.recyclerView.adapter = adapter
        }

        adapter.notifyDataSetChanged()
    }

    private fun getEmail(contactId: Long): String? {
        val emailCursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        )

        var email: String? = null
        emailCursor?.use {
            if (it.moveToFirst()) {
                val emailColumnIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                email = it.getString(emailColumnIndex)
            }
        }

        emailCursor?.close()
        return email
    }

    private fun getContactPhotoUri(rawContactId: Long): Uri? {
        Log.d("ContactsFragment", "getContactPhotoUri called")

        val photoUri = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI,
            rawContactId
        )
        Log.d("ContactsFragment", "Photo URI: $photoUri")

        val inputStream: InputStream? =
            ContactsContract.Contacts.openContactPhotoInputStream(
                requireActivity().contentResolver,
                photoUri
            )

        val result: Uri? = if (inputStream != null) {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            Log.d("ContactsFragment", "Bitmap decoded successfully")
            saveImageToInternalStorage(bitmap, rawContactId.toString()) // 저장된 파일의 Uri를 반환
        } else {
            // 연락처에 사진이 없을 경우
            Log.d("ContactsFragment", "No contact photo found, using default URI")
            Uri.parse("android.resource://com.example.anycall/drawable/user")
        }

        Log.d("ContactsFragment", "가져온 사진: $result")
        return result
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap, fileName: String): Uri? {
        Log.d("ContactsFragment", "saveImageToInternalStorage called")
        // 내부 저장소에 이미지를 저장하고 해당 파일의 Uri를 반환합니다.
        val wrapper = ContextWrapper(requireContext())
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "$fileName.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            return Uri.parse(file.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ContactsFragment", "Failed to save image to internal storage: ${e.message}")
        }
        // 이미지 저장에 실패한 경우 null을 반환
        return null
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
    ): View {
        initPermission()
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
                val btnOff = v1.findViewById<Button>(R.id.btn_notify_off)
                val btnFive = v1.findViewById<Button>(R.id.btn_notify_five)
                val btnTen = v1.findViewById<Button>(R.id.btn_notify_ten)
                val btnFifteen = v1.findViewById<Button>(R.id.btn_notify_fifteen)

                userImg.setOnClickListener {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(intent, DEFAULT_GALLERY_REQUEST_CODE)
                }
                btnOff.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
                            // 알림 권한이 없다면, 사용자에게 권한 요청
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
                            }
                            requireActivity().startActivity(intent)
                        }
                    }
                    sendNotification()
                }
                btnFive.setOnClickListener {

                }
                btnTen.setOnClickListener {

                }
                btnFifteen.setOnClickListener {

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

    private fun sendNotification() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(requireContext(), MyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // 알림을 보낼 시간을 설정합니다. 여기서는 5초 후로 설정했습니다.
        val futureInMillis = SystemClock.elapsedRealtime() + 5000
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
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