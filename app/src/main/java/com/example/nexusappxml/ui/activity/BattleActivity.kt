package com.example.nexusappxml.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.network.RetrofitClient
import kotlinx.coroutines.launch

class BattleActivity : AppCompatActivity() {

    private lateinit var battleHeroImageViews: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        val imgBackground = findViewById<ImageView>(R.id.imgBackground)
        val txtResult = findViewById<TextView>(R.id.txtBattleResult)
        val txtPowerInfo = findViewById<TextView>(R.id.txtPowerInfo)
        val btnBack = findViewById<Button>(R.id.btnBackToMenu)

        // Inicializa os 6 ImageViews da equipa em batalha
        battleHeroImageViews = listOf(
            findViewById(R.id.imgBattleHero1),
            findViewById(R.id.imgBattleHero2),
            findViewById(R.id.imgBattleHero3),
            findViewById(R.id.imgBattleHero4),
            findViewById(R.id.imgBattleHero5),
            findViewById(R.id.imgBattleHero6)
        )

        val heroIds = intent.getIntegerArrayListExtra("EXTRA_DECK_HERO_IDS") ?: arrayListOf()
        val heroImages = intent.getStringArrayListExtra("EXTRA_DECK_HERO_IMAGES") ?: arrayListOf()
        val floor = intent.getIntExtra("EXTRA_FLOOR", 1)
        val placeId = intent.getIntExtra("EXTRA_PLACE_ID", 1)

        if (heroIds.size != 6) {
            Toast.makeText(this, "Deck incompleto!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Carrega visualmente os 6 heróis escolhidos no ecrã de batalha
        for (i in battleHeroImageViews.indices) {
            if (i < heroImages.size && !heroImages[i].isNullOrEmpty()) {
                Glide.with(this)
                    .load(heroImages[i])
                    .centerCrop()
                    .into(battleHeroImageViews[i])
            }
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getInstance(this@BattleActivity).startBattle(
                    floor = floor, placeId = placeId,
                    h1 = heroIds[0], h2 = heroIds[1], h3 = heroIds[2],
                    h4 = heroIds[3], h5 = heroIds[4], h6 = heroIds[5]
                )

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!

                    val bgImage = data.place?.image ?: ""
                    val playerPower = data.playerPower
                    val enemyPower = data.enemyPower
                    val status = data.status
                    val message = data.message

                    // Carrega o fundo dinâmico do Place com o Glide
                    if (bgImage.isNotEmpty()) {
                        Glide.with(this@BattleActivity)
                            .load(bgImage)
                            .centerCrop()
                            .into(imgBackground)
                    }

                    // Mostra os poderes e o resultado na tela
                    txtPowerInfo.text = "Poder da Equipa: $playerPower  VS  Inimigo: $enemyPower"
                    txtResult.text = message

                    if (status == "victory") {
                        txtResult.setTextColor(android.graphics.Color.GREEN)
                    } else {
                        txtResult.setTextColor(android.graphics.Color.RED)
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                    Log.e("BattleActivity", "Erro do Servidor: $errorBody")
                    Toast.makeText(this@BattleActivity, "Erro: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("BattleActivity", "Erro de ligação: ${e.message}", e)
                Toast.makeText(this@BattleActivity, "Erro de ligação", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}