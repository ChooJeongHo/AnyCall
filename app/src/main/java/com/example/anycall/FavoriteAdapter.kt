package com.example.anycall

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anycall.databinding.ItemFavoritesBinding

class FavoriteAdapter(): ListAdapter<MyItem, FavoriteAdapter.FavoriteViewHolder>(diffUtil) {

    interface OnItemClickListener {
        fun onItemClick(data: MyItem, pos: Int)
    }
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoritesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyItem) {
            with(binding) {
                Glide.with(root)
                    .load(item.icon)
                    .into(itemFavoriteImage)
                itemFavoriteName.text = item.name
                itemFavoriteCall.setOnClickListener {
                    listener?.onItemClick(item, adapterPosition)
                }

            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MyItem>() {
            override fun areItemsTheSame(oldItem: MyItem, newItem: MyItem): Boolean {
                return oldItem.phoneNum == newItem.phoneNum
            }

            override fun areContentsTheSame(oldItem: MyItem, newItem: MyItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}