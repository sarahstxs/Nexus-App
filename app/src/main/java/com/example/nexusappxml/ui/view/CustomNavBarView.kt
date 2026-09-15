package com.example.nexusappxml.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nexusappxml.R

class CustomNavBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // Criando o Enum para as abas (agora como 'enum class')
    enum class Aba {
        BUY,
        COLLECTION,
        BATTLE,
        PODIUM,
        WHO
    }

    private val btnBattle: TextView?
    private val btnCollection: TextView?
    private val btnBuy: TextView?
    private val btnPodium: TextView?
    private val btnWho: TextView?

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_nav_bar, this, true)

        // Mapeando os elementos do XML
        btnBattle = findViewById(R.id.btn_battle)
        btnCollection = findViewById(R.id.btn_collection)
        btnBuy = findViewById(R.id.btn_buy)
        btnPodium = findViewById(R.id.btn_podium)
        btnWho = findViewById(R.id.btn_who)
    }

    fun setAbaAtiva(abaAtual: Aba) {
        // Reseta todos para a cor inativa
        val inactiveColor = Color.parseColor("#FFFFFF")

        // O "?." já faz a checagem de nulo por baixo dos panos (igual ao seu if != null no Java)
        btnBattle?.setTextColor(inactiveColor)
        btnCollection?.setTextColor(inactiveColor)
        btnBuy?.setTextColor(inactiveColor)
        btnPodium?.setTextColor(inactiveColor)
        btnWho?.setTextColor(inactiveColor)

        // Destaca apenas a aba atual
        val activeColor = Color.parseColor("#ee9b00")

        when (abaAtual) {
            Aba.BATTLE -> btnBattle?.setTextColor(activeColor)
            Aba.BUY -> btnBuy?.setTextColor(activeColor)
            Aba.COLLECTION -> btnCollection?.setTextColor(activeColor)
            Aba.PODIUM -> btnPodium?.setTextColor(activeColor)
            Aba.WHO -> btnWho?.setTextColor(activeColor)
        }
    }
}