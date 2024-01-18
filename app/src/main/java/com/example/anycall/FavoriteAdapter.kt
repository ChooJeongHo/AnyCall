package com.example.anycall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anycall.databinding.ItemFavoritesBinding

class FavoriteAdapter(private val emptyView: View): ListAdapter<MyItem, FavoriteAdapter.FavoriteViewHolder>(diffUtil) {
    private lateinit var binding: ItemFavoritesBinding
    interface OnItemClickListener {
        fun onPhoneClick(data: MyItem, pos: Int)
        fun onItemClick(data: MyItem)
    }
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCurrentListChanged(
        previousList: MutableList<MyItem>,
        currentList: MutableList<MyItem>
    ) {
        if (currentList.size == 0) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
        super.onCurrentListChanged(previousList, currentList)
    }

    inner class FavoriteViewHolder(binding: ItemFavoritesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyItem) {
            with(binding) {
                Glide.with(root)
                    .load(item.icon)
                    .into(itemFavoriteImage)
                itemFavoriteName.text = item.name
                itemFavoriteCall.setOnClickListener {
                    listener?.onPhoneClick(item, adapterPosition)
                }
                root.setOnClickListener {
                    listener?.onItemClick(item)
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