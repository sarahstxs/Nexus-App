package com.example.nexusappxml.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.DeckPreviewView
import com.example.nexusappxml.ui.view.GoBattleButton
import com.example.nexusappxml.ui.view.PerfilPreviewView

class CollectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_collection)
//        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
//        val coinsView = findViewById<CoinView>(R.id.coin_view)

        val userIdReal = TokenManager.getUserId(this)
        // Chame a função passando o ID do usuário logado
//        perfilPreview.loadDatas(userIdReal)

        // Chame a função passando o ID do usuário logado
//        coinsView.loadCoins(userIdReal)

        // Avisa a barra que o usuário está na tela de battle
        navBar.setAbaAtiva(CustomNavBarView.Aba.COLLECTION)
    }
}