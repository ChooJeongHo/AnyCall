package com.example.anycall

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        return favoriteList.size
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoritesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyItem) {
            with(binding) {
                ivFavoriteProfile.setImageURI(item.icon)
                tvFavoriteName.text = item.name
            }
        }
    }
}