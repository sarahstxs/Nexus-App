package com.example.nexusappxml.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusappxml.data.model.PackItem
import com.example.nexusappxml.ui.view.PackListView

class PackAdapter(
    private var packList: List<PackItem>,
    private val onBuyClick: (PackItem) -> Unit
) : RecyclerView.Adapter<PackAdapter.PackViewHolder>() {

    class PackViewHolder(val itemRowView: PackListView) : RecyclerView.ViewHolder(itemRowView) {
        fun bind(pack: PackItem, onBuyClick: (PackItem) -> Unit) {
            itemRowView.bind(
                packName = pack.name,
                packPrice = pack.price,
                packDeck = pack.deck
            )

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
        val pack = packList[position]
        holder.bind(pack, onBuyClick)
    }

    override fun getItemCount(): Int = packList.size

    fun updateData(newPacks: List<PackItem>) {
        this.packList = newPacks
        notifyDataSetChanged()
    }
}