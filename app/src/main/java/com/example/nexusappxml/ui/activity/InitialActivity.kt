package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.DeckPreviewView
import com.example.nexusappxml.ui.view.GoBattleButton
import com.example.nexusappxml.ui.view.PerfilPreviewView

class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial)

        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val coinsView = findViewById<CoinView>(R.id.coin_view)
        val btnGoBattle = findViewById<GoBattleButton>(R.id.btn_go_battle)
        val btnGoDeckPage = findViewById<DeckPreviewView>(R.id.btn_go_deck)

        val userIdReal = TokenManager.getUserId(this)

        // Carrega dados do perfil e moedas
        perfilPreview.loadDatas(userIdReal)
        coinsView.loadCoins(userIdReal)

        // Avisa a barra que o usuário está na tela de battle (ou inicial)
        navBar.setAbaAtiva(CustomNavBarView.Aba.BATTLE)

        // Configura a ação de clique vinda da barra de navegação
        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.COLLECTION -> {
                    Log.d("RETURN", "Collection button")
                    val intent = Intent(this, CollectionActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                }
                CustomNavBarView.Aba.BUY -> {
                    Log.d("RETURN", "Buy button")
                    val intent = Intent(this, BuyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                }
                CustomNavBarView.Aba.PODIUM -> {
                    Log.d("RETURN", "Podium button")
                    val intent = Intent(this, PodiumActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                }
                CustomNavBarView.Aba.WHO -> {
                    Log.d("RETURN", "Who button")
                    Toast.makeText(this@InitialActivity, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        } // <--- Chave do lambda fechada corretamente aqui

        // Adiciona o botão para batalhar
        btnGoBattle.GotoBattle()

        // Adicionar preview do deck
        btnGoDeckPage.GotoDeckPage()

        // Botão de sair/voltar para o login
        val formUsernameEmail = findViewById<Button>(R.id.exit)
        formUsernameEmail.setOnClickListener { backToLogin() }
    } // <--- Fim do onCreate

    fun backToLogin() {
        TokenManager.clearToken(this@InitialActivity)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}