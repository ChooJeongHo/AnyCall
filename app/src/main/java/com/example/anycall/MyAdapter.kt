package com.example.anycall


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.anycall.databinding.ItemBinding
import com.example.anycall.databinding.ItemGridBinding

class MyAdapter(val mItems: MutableList<MyItem>) : RecyclerView.Adapter<MyAdapter.Holder>(),
    ItemTouchHelperListener {
    private var recyclerView: RecyclerView? = null
    private val alterBackColors = arrayOf(
        R.color.white,
        R.color.mainColor
    )

    companion object {
        const val MY_PERMISSIONS_REQUEST_CALL_PHONE = 123 // You can use any unique value
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    private var isGridView = false

    fun setGridView(isGridView: Boolean) {
        this.isGridView = isGridView
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) 1 else 0
    }

    /**
     * swipe할때
     */
    private val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(this))
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onItemSwipe(position: Int) {
        val context = recyclerView?.context
        val phoneNumber = mItems[position].phoneNum
        if (!mItems[position].isSwiped) {
            mItems[position].isSwiped = true // 스와이프 상태 변경
            notifyItemChanged(position) // 아이템 변경을 알림
        }
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        context?.startActivity(callIntent)

    }

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = if (viewType == 1) {
            ItemGridBinding.inflate(layoutInflater, parent, false)
        } else {
            ItemBinding.inflate(layoutInflater, parent, false)
        }
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val backgroundColor = ContextCompat.getColor(
            holder.itemView.context,
            alterBackColors[position % alterBackColors.size]
        )
        holder.itemView.setBackgroundColor(backgroundColor)
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.iconImageView.setImageURI(mItems[position].icon)
        holder.name.text = mItems[position].name
        holder.like.setImageResource(mItems[position].like)

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("삭제 확인")
                .setMessage("${mItems[position].name} 연락처를 삭제하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    mItems.removeAt(position)
                    notifyItemRemoved(position)
                }
                .setNegativeButton("아니오", null)
                .show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    /*inner class Holder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val iconImageView = binding.itemImage
        val name = binding.itemName
        val like = binding.likeImage
    }*/

    inner class Holder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val iconImageView = when (binding) {
            is ItemBinding -> binding.itemImage
            is ItemGridBinding -> binding.itemImage
            else -> throw IllegalArgumentException("Invalid view binding")
        }
        val name = when (binding) {
            is ItemBinding -> binding.itemName
            is ItemGridBinding -> binding.itemName
            else -> throw IllegalArgumentException("Invalid view binding")
        }
        val like = when (binding) {
            is ItemBinding -> binding.likeImage
            is ItemGridBinding -> binding.likeImage
            else -> throw IllegalArgumentException("Invalid view binding")
        }
    }

}