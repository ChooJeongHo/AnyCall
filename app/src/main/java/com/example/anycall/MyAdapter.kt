package com.example.anycall

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anycall.databinding.ItemBinding

class MyAdapter(val mItems: MutableList<MyItem>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    interface ItemClick {
        fun onClick (view: View, position: Int)
    }

    var itemClick: ItemClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
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

    inner class Holder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val iconImageView = binding.itemImage
        val name = binding.itemName
        val like = binding.likeImage
    }

}