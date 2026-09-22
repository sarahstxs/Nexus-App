package com.example.nexusappxml

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.ui.activity.BuyActivity
import com.example.nexusappxml.ui.activity.InitialActivity
import com.example.nexusappxml.ui.activity.PodiumActivity
import com.example.nexusappxml.ui.view.AlbumAdapter
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.PerfilPreviewView

class MyCollectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_collection)
//        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val coinsView = findViewById<CoinView>(R.id.coin_view)


//        val coinsView = findViewById<CoinView>(R.id.coin_view)

        val userIdReal = TokenManager.getUserId(this)

        // Carrega dados do perfil e moedas
        perfilPreview.loadDatas(userIdReal)
        coinsView.loadCoins(userIdReal)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAlbuns)

// Exemplo em Grade (Grid) com 2 colunas
        recyclerView.layoutManager = GridLayoutManager(this, 3)

// Lista de URLs de exemplo apenas com imagens
        val minhasImagens = listOf(
            "https://comicvine.gamespot.com/a/uploads/original/11161/111612243/10012902-5140161970-b364e.jpg",
            "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
            "https://images.unsplash.com/photo-1511285560929-80b456fea0bc",
            "https://comicvine.gamespot.com/a/uploads/original/11161/111612243/10012902-5140161970-b364e.jpg",
            "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
            "https://images.unsplash.com/photo-1511285560929-80b456fea0bc",
            "https://comicvine.gamespot.com/a/uploads/original/11161/111612243/10012902-5140161970-b364e.jpg",
            "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
            "https://images.unsplash.com/photo-1511285560929-80b456fea0bc",
            "https://comicvine.gamespot.com/a/uploads/original/11161/111612243/10012902-5140161970-b364e.jpg",
            "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
            "https://images.unsplash.com/photo-1511285560929-80b456fea0bc",
            "https://comicvine.gamespot.com/a/uploads/original/11161/111612243/10012902-5140161970-b364e.jpg",
            "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
            "https://images.unsplash.com/photo-1511285560929-80b456fea0bc",
            "https://comicvine.gamespot.com/a/uploads/original/11161/111612243/10012902-5140161970-b364e.jpg",
            "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
            "https://images.unsplash.com/photo-1511285560929-80b456fea0bc"
        )

        recyclerView.adapter = AlbumAdapter(minhasImagens)

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