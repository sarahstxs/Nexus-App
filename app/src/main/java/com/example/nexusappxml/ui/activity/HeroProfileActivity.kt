package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.data.network.RetrofitClient
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.ImageHeroProfileView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroProfileActivity : AppCompatActivity() {

    private var selectedHeroId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hero_profile)

        selectedHeroId = intent.getIntExtra("HEROI_ID", -1)

        val imgBackground = findViewById<ImageView>(R.id.imgBackground)

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        if (selectedHeroId != -1) {
            fetchHeroDetails(selectedHeroId)
        } else {
            Toast.makeText(this, "Error: Hero not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupNavBar()
    }

    private fun fetchHeroDetails(heroId: Int) {
        val userId = TokenManager.getUserId(this)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getInstance(this@HeroProfileActivity)
                    .getHeroComplete(heroId = heroId, userId = userId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val heroResponse = response.body()!!
                        val baseHero = heroResponse.hero
                        val userHero = heroResponse.userHero

                        if (baseHero != null) {

                            val isOwned = heroResponse.have
                            val level = if (isOwned && userHero != null) userHero.level else 1
                            val fragments = if (isOwned && userHero != null) userHero.fragments else 0

                            val currentHp = if (isOwned && userHero != null) userHero.currentHp else baseHero.baseHp
                            val maxHp = if (isOwned && userHero != null) userHero.maxHp else baseHero.baseHp
                            val rarityName = when(baseHero.rarity) {
                                1 -> "Common"
                                2 -> "Uncommon"
                                3 -> "Rare"
                                4 -> "Epic"
                                5 -> "Legendary"
                                6 -> "Mythic"
                                else -> "Invalid"
                            }

                            // MAPPING ALL VIEWS
                            val heroProfileImg = findViewById<ImageHeroProfileView>(R.id.componenteImgHero)
                            val txtHeroName = findViewById<TextView>(R.id.txtHeroName)
                            val txtRealName = findViewById<TextView>(R.id.txtRealName)
                            val txtHave = findViewById<TextView>(R.id.txtHave)
                            val viewHave = findViewById<View>(R.id.viewHave)

                            val txtRarity = findViewById<TextView>(R.id.txtRarity)
                            val txtClass = findViewById<TextView>(R.id.txtClass)
                            val txtOrigin = findViewById<TextView>(R.id.txtOrigin)
                            val txtLevel = findViewById<TextView>(R.id.txtLevel)
                            val txtBirth = findViewById<TextView>(R.id.txtBirth)
                            val txtAppearance = findViewById<TextView>(R.id.txtAppearance)
                            val txtFirstAppearanceComic = findViewById<TextView>(R.id.txtFirstAppearanceComic)
                            val txtNemesis = findViewById<TextView>(R.id.txtNemesis)
                            val txtGender = findViewById<TextView>(R.id.txtGender)
                            val txtDeck = findViewById<TextView>(R.id.txtDeck)

                            val txtFragments = findViewById<TextView>(R.id.txtFragments)
                            val txtAttack = findViewById<TextView>(R.id.txtAttack)
                            val txtLife = findViewById<TextView>(R.id.txtLife)
                            val txtDefense = findViewById<TextView>(R.id.txtDefense)
                            val txtHyperAttack = findViewById<TextView>(R.id.txtHyperAttack)

                            // ==========================================
                            // POPULATING DATA WITH HTML FORMATTING
                            // ==========================================
                            heroProfileImg.bind(baseHero.imageHero ?: "")

                            txtHeroName.text = "${baseHero.name}"
                            txtRealName.text = baseHero.realName ?: "Unknown Identity"


                            txtHave.setTextColor(Color.parseColor("#FFFFFF")) // Espaço extra removido aqui

                            if (isOwned) {
                                txtHave.text = "Obtained"
                                viewHave.setBackgroundColor(Color.parseColor("#125c0e"))
                            } else {
                                txtHave.text = "Not obtained"
                                viewHave.setBackgroundColor(Color.parseColor("#D32F2F"))
                            }

                            // Variável com a cor vermelha para facilitar caso queira mudar depois
                            val c = "#c24044"
                            val a = "#ee9b00"
                            val rarityColor = when (rarityName.lowercase()) {
                                "common" -> "#B0BEC5"       // Cinza claro / Prateado (Limpo e neutro)
                                "uncommon" -> "#81C784"     // Verde claro (Fácil de ver)
                                "rare" -> "#64B5F6"         // Azul claro brilhante
                                "epic" -> "#CE93D8"         // Roxo claro / Lavanda vibrante
                                "legendary" -> "#FFD54F"    // Amarelo / Dourado solar
                                "mythic" -> "#FF5252"       // Vermelho / Coral vivo (Destaque máximo)
                                else -> "#EF5350"           // Vermelho padrão claro (caso venha algo desconhecido)
                            }

                            Log.d("TESTE", "${baseHero}")

                            // Base Details (Label em negrito e vermelho, Valor normal e preto)
                            txtRarity.text = HtmlCompat.fromHtml("<font color='$rarityColor'> ${rarityName} </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtClass.text = HtmlCompat.fromHtml("<b><font color='$c'>Class:</font></b> ${baseHero.classHero}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtLevel.text = HtmlCompat.fromHtml("<b><font color='$c'>Level:</font></b> Level $level", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtOrigin.text = HtmlCompat.fromHtml("<b><font color='$c'>Origin:</font></b> ${baseHero.origin ?: "Unknown"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtBirth.text = HtmlCompat.fromHtml("<b><font color='$c'>Birth:</font></b> ${baseHero.birth ?: "Unknown"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtAppearance.text = HtmlCompat.fromHtml("<b><font color='$c'>Appearances:</font></b> ${baseHero.appearance ?: "Unknown"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtFirstAppearanceComic.text = HtmlCompat.fromHtml("<b><font color='$c'>First comic:</font></b> ${baseHero.firstAppearanceComic ?: "Unknown"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtNemesis.text = HtmlCompat.fromHtml("<b><font color='$c'>Nemesis:</font></b> ${baseHero.nemesis ?: "None"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtGender.text = HtmlCompat.fromHtml("<b><font color='$c'>Gender:</font></b> ${baseHero.gender ?: "Unknown"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtDeck.text = HtmlCompat.fromHtml("<b><font color='$c'>Deck:</font></b> ${baseHero.deck ?: "None"}", HtmlCompat.FROM_HTML_MODE_LEGACY)

                            // Attributes
                            txtFragments.text = HtmlCompat.fromHtml("<b><font color='$c'>Fragments:</font></b> $fragments", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtAttack.text = HtmlCompat.fromHtml("<b> ${baseHero.baseAtk}<br></b><font color='$a'> Ataque</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtLife.text = HtmlCompat.fromHtml("<b>$currentHp / $maxHp<br></b><font color='$a'>Health </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtDefense.text = HtmlCompat.fromHtml("<b>${baseHero.baseDef}<br></b><font color='$a'>Defense </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtHyperAttack.text = HtmlCompat.fromHtml("<b><font color='$c'>Hiper ataque:</font></b> ${baseHero.hyperAttack}", HtmlCompat.FROM_HTML_MODE_LEGACY)

                        } else {
                            Toast.makeText(this@HeroProfileActivity, "Corrupted hero data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("HERO_API_ERROR", "Error Code: ${response.code()}")
                        Toast.makeText(this@HeroProfileActivity, "Failed to load hero details", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("API_ERROR", "Error: ${e.message}")
                    Toast.makeText(this@HeroProfileActivity, "Connection failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupNavBar() {
        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        navBar.setAbaAtiva(CustomNavBarView.Aba.COLLECTION)
        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.BATTLE -> {
                    startActivity(Intent(this, InitialActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION })
                }
                CustomNavBarView.Aba.BUY -> {
                    startActivity(Intent(this, BuyActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION })
                }
                CustomNavBarView.Aba.PODIUM -> {
                    startActivity(Intent(this, PodiumActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION })
                }
                CustomNavBarView.Aba.COLLECTION -> {
                    startActivity(Intent(this, ChoseCollectionActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION })
                }
                CustomNavBarView.Aba.WHO -> {
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}