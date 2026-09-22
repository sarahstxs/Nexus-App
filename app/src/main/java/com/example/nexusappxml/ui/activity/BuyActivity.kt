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
import com.example.nexusappxml.ui.view.CustomNavBarView

class BuyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buy)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)

        navBar.setAbaAtiva(CustomNavBarView.Aba.BUY)

        // Configura a ação de clique vinda da barra de navegação
        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.COLLECTION -> {
                    Log.d("RETURN", "Collection button")
                    val intent = Intent(this, ChoseCollectionActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                }
                CustomNavBarView.Aba.BATTLE -> {
                    Log.d("RETURN", "Battle button")
                    val intent = Intent(this, InitialActivity::class.java)
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
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

    }
}