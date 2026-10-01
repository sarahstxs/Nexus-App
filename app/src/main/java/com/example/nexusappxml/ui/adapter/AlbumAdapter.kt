package com.example.nexusappxml.ui.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusappxml.data.model.HeroItem

class AlbumAdapter(
    private var heroList: List<HeroItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(val itemAlbumView: ItemAlbumView) : RecyclerView.ViewHolder(itemAlbumView) {
        fun bind(imageUrl: String) {
            itemAlbumView.bind(imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val customView = ItemAlbumView(parent.context)
        return AlbumViewHolder(customView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val hero = heroList[position]

        // Passes the URL to load the image
        holder.bind(hero.imageUrl)

        holder.itemView.setOnClickListener {
            onItemClick(hero.id)
        }
    }

    override fun getItemCount(): Int = heroList.size

    fun updateData(newHeroes: List<HeroItem>) {
        this.heroList = newHeroes
        notifyDataSetChanged()
    }
}