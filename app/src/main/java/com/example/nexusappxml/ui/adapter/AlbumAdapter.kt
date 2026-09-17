package com.example.nexusappxml.ui.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(private val listaImagens: List<String>) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(val itemAlbumView: ItemAlbumView) : RecyclerView.ViewHolder(itemAlbumView) {
        fun bind(imageUrl: String) {
            itemAlbumView.bind(imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        // Cria uma instância do seu componente customizado para cada linha da lista
        val customView = ItemAlbumView(parent.context)
        return AlbumViewHolder(customView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val imageUrl = listaImagens[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int = listaImagens.size
}