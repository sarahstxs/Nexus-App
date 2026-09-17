package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        // Chama a função passando o ID do usuário logado
//        perfilPreview.loadDatas(userIdReal)

        // Chama a função passando o ID do usuário logado
//        coinsView.loadCoins(userIdReal)

        // Avisa a barra que o usuário está na tela de battle
        navBar.setAbaAtiva(CustomNavBarView.Aba.COLLECTION)

        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.BATTLE -> {
                    Log.d("RETURN", "Battle button")
                    val intent = Intent(this, InitialActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.BUY -> {
                    Log.d("RETURN", "Buy button")
                    val intent = Intent(this, BuyActivity::class.java).apply {
                        // Combina as flags corretamente usando 'or'
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.PODIUM -> {
                    Log.d("RETURN", "Podium button")
                    val intent = Intent(this, PodiumActivity::class.java).apply {
                        // Combina as flags corretamente usando 'or'
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.WHO -> {
                    Log.d("RETURN", "Who button")
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}