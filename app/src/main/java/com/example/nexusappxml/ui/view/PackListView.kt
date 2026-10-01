package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nexusappxml.R

class PackListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val txtTitulo: TextView
    private val txtPreco: TextView
    private val txtDeck: TextView
    private val btnBuy: LinearLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pack_list, this, true)

        txtTitulo = findViewById(R.id.txtTitulo)
        txtPreco = findViewById(R.id.txtPrice)
        txtDeck = findViewById(R.id.txtDeck)
        btnBuy = findViewById(R.id.btnBuy)

        layoutParams = MarginLayoutParams(
            MarginLayoutParams.MATCH_PARENT,
            MarginLayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 6, 0, 6)
        }
    }

    fun bind(packName: String, packPrice: Int, packDeck: String) {
        txtTitulo.text = packName
        txtPreco.text = "$packPrice coins"
        txtDeck.text = packDeck
    }

    fun setOnBuyClickListener(listener: () -> Unit) {
        btnBuy.setOnClickListener { listener() }
    }
}