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

    // Declaramos todas as variáveis como propriedades da classe
    private val txtTitulo: TextView
    private val txtPreco: TextView
    private val txtDeck: TextView
    private val btnBuy: LinearLayout // Agora é acessível na classe toda

    init {
        // 1. PRIMEIRO: Infla o layout XML na tela
        LayoutInflater.from(context).inflate(R.layout.view_pack_list, this, true)

        // 2. DEPOIS: Faz o findViewById com segurança
        txtTitulo = findViewById(R.id.txtTitulo)
        txtPreco = findViewById(R.id.txtPrice)
        txtDeck = findViewById(R.id.txtDeck)
        btnBuy = findViewById(R.id.btnBuy) // Verifique se o ID no XML é realmente btnBuy

        // 3. Ajustamos os parâmetros de margem do item da lista
        layoutParams = MarginLayoutParams(
            MarginLayoutParams.MATCH_PARENT,
            MarginLayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 6, 0, 6)
        }
    }

    // Função para preencher os dados que vêm da API
    fun bind(packName: String, packPrice: Int, packDeck: String) {
        txtTitulo.text = packName
        txtPreco.text = "$packPrice coins"
        txtDeck.text = packDeck
    }

    // Função essencial para o Adapter conseguir acionar a compra quando o botão for clicado
    fun setOnBuyClickListener(listener: () -> Unit) {
        btnBuy.setOnClickListener { listener() }
    }
}