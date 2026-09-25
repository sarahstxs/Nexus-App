package com.example.nexusappxml.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusappxml.R
import com.example.nexusappxml.data.model.RankUser

class RankAdapter(private var rankList: List<RankUser>) :
    RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    class RankViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtPosition: TextView = view.findViewById(R.id.txtPosition)
        val txtUserName: TextView = view.findViewById(R.id.txtUserName)
        val txtUserLevel: TextView = view.findViewById(R.id.txtUserLevel)

        fun bind(user: RankUser) {
            txtPosition.text = "#${user.position}"
            txtUserName.text = user.name
            txtUserLevel.text = "Nível ${user.level}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rank_row, parent, false)
        return RankViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bind(rankList[position])
    }

    override fun getItemCount(): Int = rankList.size

    fun updateData(newList: List<RankUser>) {
        this.rankList = newList
        notifyDataSetChanged()
    }
}