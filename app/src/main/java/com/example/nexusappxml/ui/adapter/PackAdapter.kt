package com.example.nexusappxml.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusappxml.data.model.PackItem
import com.example.nexusappxml.ui.view.PackListView

class PackAdapter(
    private var listaPacks: List<PackItem>,
    private val onBuyClick: (PackItem) -> Unit
) : RecyclerView.Adapter<PackAdapter.PackViewHolder>() {

    class PackViewHolder(val itemRowView: PackListView) : RecyclerView.ViewHolder(itemRowView) {
        fun bind(pack: PackItem, onBuyClick: (PackItem) -> Unit) {
            // CORREÇÃO: Adicionamos o parâmetro 'packDeck' que estava faltando aqui
            itemRowView.bind(
                packName = pack.name,
                packPrice = pack.price,
                packDeck = pack.deck
            )

            // Configura o clique no botão "Buy Pack"
            itemRowView.setOnBuyClickListener {
                onBuyClick(pack)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackViewHolder {
        val customView = PackListView(parent.context)
        return PackViewHolder(customView)
    }

    override fun onBindViewHolder(holder: PackViewHolder, position: Int) {
        val pack = listaPacks[position]
        holder.bind(pack, onBuyClick)
    }

    override fun getItemCount(): Int = listaPacks.size

    fun updateData(newPacks: List<PackItem>) {
        this.listaPacks = newPacks
        notifyDataSetChanged()
    }
}