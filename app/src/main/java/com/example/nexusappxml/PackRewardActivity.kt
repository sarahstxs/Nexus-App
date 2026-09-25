package com.example.nexusappxml.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.model.WonHeroRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PackRewardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pack_reward)

        val container = findViewById<LinearLayout>(R.id.layoutHeroesContainer)
        val btnContinuar = findViewById<Button>(R.id.btnColher)

        // Resgata o JSON enviado pela Intent com os heróis sorteados
        val heroesJson = intent.getStringExtra("WON_HEROES_JSON")
        Log.d("PACK_REWARD", "JSON recebido: $heroesJson")

        if (!heroesJson.isNullOrEmpty()) {
            try {
                val listType = object : TypeToken<List<WonHeroRequest>>() {}.type
                val heroes: List<WonHeroRequest>? = Gson().fromJson(heroesJson, listType)

                heroes?.forEachIndexed { index, hero ->
                    // Infla o novo layout criado especificamente para as recompensas
                    val itemView = layoutInflater.inflate(R.layout.view_reward_hero, container, false).apply {
                        alpha = 0f
                        scaleX = 0.5f
                        scaleY = 0.5f
                    }

                    val txtHeroName = itemView.findViewById<TextView>(R.id.txtHeroName)
                    val imgHeroReward = itemView.findViewById<ImageView>(R.id.imgHeroReward)

                    // Define o nome do herói obtido na API
                    txtHeroName.text = hero.name ?: "Herói Desconhecido"

                    // Carrega a imagem via Glide com segurança
                    if (!hero.imageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(hero.imageUrl)
                            .placeholder(R.drawable.shadow_perfil_icon)
                            .centerCrop()
                            .into(imgHeroReward)
                    } else {
                        imgHeroReward.setImageResource(R.drawable.shadow_perfil_icon)
                    }

                    container.addView(itemView)

                    // Animação de surgimento em cascata para cada card
                    itemView.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(2000)
                        .setStartDelay((index * 200).toLong())
                        .start()
                }
            } catch (e: Exception) {
                Log.e("PACK_REWARD", "Erro ao processar JSON dos heróis: ${e.message}")
            }
        }

        btnContinuar.setOnClickListener {
            finish() // Fecha a tela de recompensa e retorna para a loja
        }
    }
}