package com.example.anycall

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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