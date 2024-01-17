package com.example.anycall

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anycall.databinding.ItemFavoritesBinding

class FavoriteAdapter(private val favoriteList: MutableList<MyItem>): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    override fun getItemCount(): Int {
        Log.d("FavoriteAdapterSize:",favoriteList.size.toString())
        return favoriteList.size
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoritesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyItem) {
            with(binding) {
                Glide.with(root)
                    .load(item.icon)
                    .into(itemFavoriteImage)

            }
        }
    }
}