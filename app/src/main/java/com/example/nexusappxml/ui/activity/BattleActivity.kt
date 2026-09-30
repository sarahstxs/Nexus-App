package com.example.nexusappxml.ui.activity

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se preferir manter o layout anterior, altere para R.layout.activity_battle
        setContentView(R.layout.activity_battle)

        val imgBackground = findViewById<ImageView>(R.id.imgBackground)
        val txtResult = findViewById<TextView>(R.id.txtBattleResult)
        val txtPowerInfo = findViewById<TextView>(R.id.txtPowerInfo)
        val btnBack = findViewById<Button>(R.id.btnBackToMenu)

        val heroIds = intent.getIntegerArrayListExtra("EXTRA_DECK_HERO_IDS") ?: arrayListOf()
        val floor = intent.getIntExtra("EXTRA_FLOOR", 1)
        val placeId = intent.getIntExtra("EXTRA_PLACE_ID", 1)

        if (heroIds.size != 6) {
            Toast.makeText(this, "Deck incompleto!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getInstance(this@BattleActivity).resolveBattle(
                    floor = floor, placeId = placeId,
                    h1 = heroIds[0], h2 = heroIds[1], h3 = heroIds[2],
                    h4 = heroIds[3], h5 = heroIds[4], h6 = heroIds[5]
                )

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!

                    val bgImage = data.placeImage
                    val playerPower = data.playerPower
                    val enemyPower = data.enemyPower
                    val status = data.status
                    val message = data.message

                    // Carrega o fundo dinâmico do Place com o Glide
                    Glide.with(this@BattleActivity).load(bgImage).centerCrop().into(imgBackground)

                    // Mostra os poderes e o resultado na tela
                    txtPowerInfo.text = "Poder da Equipa: $playerPower  VS  Inimigo: $enemyPower"
                    txtResult.text = message

                    if (status == "victory") {
                        txtResult.setTextColor(android.graphics.Color.GREEN)
                    } else {
                        txtResult.setTextColor(android.graphics.Color.RED)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@BattleActivity, "Erro ao processar combate", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}