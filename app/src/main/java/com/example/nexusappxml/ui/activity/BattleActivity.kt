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

        // Initialize the 6 battle team ImageViews
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
            Toast.makeText(this, "Incomplete deck!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Visually load the 6 chosen heroes on the battle screen
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

                    // Load the Place dynamic background using Glide
                    if (bgImage.isNotEmpty()) {
                        Glide.with(this@BattleActivity)
                            .load(bgImage)
                            .centerCrop()
                            .into(imgBackground)
                    }

                    // Display powers and result on screen
                    txtPowerInfo.text = "Team Power: $playerPower  VS  Enemy Power: $enemyPower"
                    txtResult.text = message

                    if (status == "victory") {
                        txtResult.setTextColor(android.graphics.Color.GREEN)
                    } else {
                        txtResult.setTextColor(android.graphics.Color.RED)
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("BattleActivity", "Server Error: $errorBody")
                    Toast.makeText(this@BattleActivity, "Error: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("BattleActivity", "Connection error: ${e.message}", e)
                Toast.makeText(this@BattleActivity, "Connection error", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}