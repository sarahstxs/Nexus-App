package com.example.nexusappxml.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nexusappxml.R
import android.content.res.ColorStateList

class CustomNavBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class Aba {
        BUY,
        COLLECTION,
        BATTLE,
        PODIUM,
        WHO
    }

    private val btnBuyContainer: LinearLayout?
    private val btnCollectionContainer: LinearLayout?
    private val btnBattleContainer: LinearLayout?
    private val btnPodiumContainer: LinearLayout?
    private val btnWhoContainer: LinearLayout?
    private val btnBattle: TextView?
    private val btnCollection: TextView?
    private val btnBuy: TextView?
    private val btnPodium: TextView?
    private val btnWho: TextView?

    private val iconBuy: ImageView?
    private val iconCollection: ImageView?
    private val iconBattle: ImageView?
    private val iconPodium: ImageView?

    var onAbaSelectedListener: ((Aba) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_nav_bar, this, true)

        // Mapping XML elements
        btnBuy = findViewById(R.id.btn_buy)
        btnBuyContainer = findViewById(R.id.btn_buy_container)
        iconBuy = findViewById(R.id.icon_buy)

        btnCollection = findViewById(R.id.btn_collection)
        btnCollectionContainer = findViewById(R.id.btn_collection_container)
        iconCollection = findViewById(R.id.icon_collection)

        btnBattle = findViewById(R.id.btn_battle)
        btnBattleContainer = findViewById(R.id.btn_battle_container)
        iconBattle = findViewById(R.id.icon_battle)

        btnPodium = findViewById(R.id.btn_podium)
        btnPodiumContainer = findViewById(R.id.btn_podium_container)
        iconPodium = findViewById(R.id.icon_podium)

        btnWho = findViewById(R.id.btn_who)
        btnWhoContainer = findViewById(R.id.btn_who_container)
    }

    fun setAbaAtiva(abaAtual: Aba) {
        // Resets all to inactive color
        val inactiveColor = Color.parseColor("#FFFFFF")

        btnBattle?.setTextColor(inactiveColor)
        btnCollection?.setTextColor(inactiveColor)
        btnBuy?.setTextColor(inactiveColor)
        btnPodium?.setTextColor(inactiveColor)
        btnWho?.setTextColor(inactiveColor)

        btnBuyContainer?.setOnClickListener {
            // Invokes the listener passing the corresponding tab
            onAbaSelectedListener?.invoke(Aba.BUY)
        }
        btnCollectionContainer?.setOnClickListener {
            // Invokes the listener passing the corresponding tab
            onAbaSelectedListener?.invoke(Aba.COLLECTION)
        }
        btnBattleContainer?.setOnClickListener {
            // Invokes the listener passing the corresponding tab
            onAbaSelectedListener?.invoke(Aba.BATTLE)
        }
        btnPodiumContainer?.setOnClickListener {
            // Invokes the listener passing the corresponding tab
            onAbaSelectedListener?.invoke(Aba.PODIUM)
        }
        btnWhoContainer?.setOnClickListener {
            // Invokes the listener passing the corresponding tab
            onAbaSelectedListener?.invoke(Aba.WHO)
        }

        // Highlights only the current tab
        val activeColor = Color.parseColor("#ee9b00")

        when (abaAtual) {
            Aba.BATTLE -> {
                btnBattle?.setTextColor(activeColor)
                iconBattle?.imageTintList = ColorStateList.valueOf(activeColor)

                btnCollection?.setTextColor(inactiveColor)
                btnBuy?.setTextColor(inactiveColor)
                btnPodium?.setTextColor(inactiveColor)
                btnWho?.setTextColor(inactiveColor)

                iconBuy?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconCollection?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconPodium?.imageTintList = ColorStateList.valueOf(inactiveColor)
            }
            Aba.BUY -> {
                btnBuy?.setTextColor(activeColor)
                iconBuy?.imageTintList = ColorStateList.valueOf(activeColor)

                btnBattle?.setTextColor(inactiveColor)
                btnCollection?.setTextColor(inactiveColor)
                btnPodium?.setTextColor(inactiveColor)
                btnWho?.setTextColor(inactiveColor)

                iconCollection?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconBattle?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconPodium?.imageTintList = ColorStateList.valueOf(inactiveColor)
            }
            Aba.COLLECTION -> {
                btnCollection?.setTextColor(activeColor)
                iconCollection?.imageTintList = ColorStateList.valueOf(activeColor)

                btnBattle?.setTextColor(inactiveColor)
                btnBuy?.setTextColor(inactiveColor)
                btnPodium?.setTextColor(inactiveColor)
                btnWho?.setTextColor(inactiveColor)

                iconBuy?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconBattle?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconPodium?.imageTintList = ColorStateList.valueOf(inactiveColor)
            }
            Aba.PODIUM -> {
                btnPodium?.setTextColor(activeColor)
                iconPodium?.imageTintList = ColorStateList.valueOf(activeColor)

                btnBattle?.setTextColor(inactiveColor)
                btnBuy?.setTextColor(inactiveColor)
                btnCollection?.setTextColor(inactiveColor)
                btnWho?.setTextColor(inactiveColor)

                iconBuy?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconBattle?.imageTintList = ColorStateList.valueOf(inactiveColor)
                iconCollection?.imageTintList = ColorStateList.valueOf(inactiveColor)
            }
            Aba.WHO -> btnWho?.setTextColor(activeColor)
        }
    }
}