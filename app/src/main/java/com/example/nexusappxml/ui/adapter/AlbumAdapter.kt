package com.example.nexusappxml.ui.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusappxml.data.model.HeroItem

// ATENÇÃO: Verifique se o import do seu HeroItem está correto aqui
// import com.example.nexusappxml.seu_pacote_de_modelo.HeroItem

class AlbumAdapter(
    // 1. Mudamos a lista principal para List<HeroItem>
    private var listaHerois: List<HeroItem>,
    // 2. Adicionamos a função de clique que vai devolver o ID
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(val itemAlbumView: ItemAlbumView) : RecyclerView.ViewHolder(itemAlbumView) {
        fun bind(imageUrl: String) {
            // Isso continua igual, o ItemAlbumView só precisa da URL
            itemAlbumView.bind(imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val customView = ItemAlbumView(parent.context)
        return AlbumViewHolder(customView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val heroi = listaHerois[position]

        // Passa a URL para carregar a imagem
        holder.bind(heroi.imageUrl)

        // Configura o clique no item para enviar o ID
        holder.itemView.setOnClickListener {
            onItemClick(heroi.id)
        }
    }

    override fun getItemCount(): Int = listaHerois.size

    // 3. AQUI ESTÁ A CORREÇÃO DO SEU ERRO: mudamos de List<String> para List<HeroItem>
    fun updateData(newHeroes: List<HeroItem>) {
        this.listaHerois = newHeroes
        notifyDataSetChanged()
    }
}