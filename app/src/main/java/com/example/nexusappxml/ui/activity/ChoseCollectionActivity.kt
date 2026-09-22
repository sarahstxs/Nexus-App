package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.nexusappxml.MyCollectionActivity
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.PerfilPreviewView

class ChoseCollectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chose_collection)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        // Esconde a barra de navegação
        controller.hide(WindowInsetsCompat.Type.navigationBars())

        // Faz com que a barra apareça apenas se o usuário arrastar de baixo para cima, e depois suma de novo
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        var imgBackground = findViewById<ImageView>(R.id.imgBackground)

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val coinsView = findViewById<CoinView>(R.id.coin_view)
        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val btnMyCollection = findViewById<FrameLayout>(R.id.btnMyCollection)
        val btnAllHeroes = findViewById<FrameLayout>(R.id.btnAllHeroes)
        val btnComingSoon = findViewById<FrameLayout>(R.id.btnComingSoon)

        btnMyCollection.setOnClickListener { goToMyCollection() }
        btnAllHeroes.setOnClickListener { goToAllHeroes() }
        btnComingSoon.setOnClickListener { goToComingSoon() }

        val userIdReal = TokenManager.getUserId(this)

        perfilPreview.loadDatas(userIdReal)
        coinsView.loadCoins(userIdReal)
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
    fun goToMyCollection() {
        TokenManager.clearToken(this)

        val intent = Intent(this, MyCollectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

    fun goToAllHeroes() {
        TokenManager.clearToken(this)

        val intent = Intent(this, CollectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
    fun goToComingSoon() {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
    }
}