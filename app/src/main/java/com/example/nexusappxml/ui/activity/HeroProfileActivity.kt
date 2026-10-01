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

        selectedHeroId = intent.getIntExtra("HERO_ID", -1)

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

                            val heroProfileImg = findViewById<ImageHeroProfileView>(R.id.componenteImgHero)
                            val txtHeroName = findViewById<TextView>(R.id.txtHeroName)
                            val txtRealName = findViewById<TextView>(R.id.txtRealName)
                            val txtHave = findViewById<TextView>(R.id.txtHave)
                            val viewHave = findViewById<View>(R.id.viewHave)

                            val txtRarity = findViewById<TextView>(R.id.txtRarity)
                            val txtLevel = findViewById<TextView>(R.id.txtLevel)
                            val txtBirth = findViewById<TextView>(R.id.txtBirth)
                            val txtAppearance = findViewById<TextView>(R.id.txtAppearance)
                            val txtFirstAppearanceComic = findViewById<TextView>(R.id.txtFirstAppearanceComic)
                            val txtGender = findViewById<TextView>(R.id.txtGender)
                            val txtDeck = findViewById<TextView>(R.id.txtDeck)

                            val txtFragments = findViewById<TextView>(R.id.txtFragments)
                            val txtAttack = findViewById<TextView>(R.id.txtAttack)
                            val txtLife = findViewById<TextView>(R.id.txtLife)
                            val txtDefense = findViewById<TextView>(R.id.txtDefense)

                            heroProfileImg.bind(baseHero.imageHero ?: "")

                            txtHeroName.text = "${baseHero.name}"
                            txtRealName.text = baseHero.realName ?: "Unknown Identity"

                            txtHave.setTextColor(Color.parseColor("#FFFFFF"))

                            if (isOwned) {
                                txtHave.text = "Obtained"
                                viewHave.setBackgroundColor(Color.parseColor("#276615"))
                            } else {
                                txtHave.text = "Not obtained"
                                viewHave.setBackgroundColor(Color.parseColor("#D32F2F"))
                            }

                            val c = "#FFFFFF"
                            val a = "#ee9b00"
                            val rarityColor = when (rarityName.lowercase()) {
                                "common" -> "#B0BEC5"       // Light gray / Silver
                                "uncommon" -> "#81C784"     // Light green
                                "rare" -> "#64B5F6"         // Bright light blue
                                "epic" -> "#CE93D8"         // Light purple / Vibrant lavender
                                "legendary" -> "#FFD54F"    // Solar yellow / Gold
                                "mythic" -> "#FF5252"       // Vibrant coral red
                                else -> "#EF5350"           // Default light red
                            }

                            Log.d("TEST", "${baseHero}")

                            // Base Details
                            txtRarity.text = HtmlCompat.fromHtml("<font color='$rarityColor'> ${rarityName} </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtLevel.text = HtmlCompat.fromHtml("<b><font color='$a'>Level<br></font></b> $level", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtBirth.text = HtmlCompat.fromHtml("<b><font color='$a'>Birth:</font></b> ${baseHero.birth ?: "Unknown"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtAppearance.text = HtmlCompat.fromHtml("<b><font color='$a'>Appearances:</font></b> ${baseHero.appearance ?: "Unknown"} times", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtFirstAppearanceComic.text = HtmlCompat.fromHtml("<b><font color='$a'>First comic:</font></b> ${baseHero.firstAppearanceComic ?: "Unknown"}", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtGender.text = HtmlCompat.fromHtml(
                                "<b><font color='$a'>Gender:</font></b> ${if (baseHero.gender == 1) "Male" else "Female"}",
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                            txtDeck.text = HtmlCompat.fromHtml("<b><font color='$a'>Deck:</font></b> ${baseHero.deck ?: "None"}", HtmlCompat.FROM_HTML_MODE_LEGACY)

                            // Attributes
                            txtFragments.text = HtmlCompat.fromHtml("<b><font color='$a'>Fragments<br></font></b> $fragments", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtAttack.text = HtmlCompat.fromHtml("<b> ${baseHero.baseAtk}<br></b><font color='$a'> Attack</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtLife.text = HtmlCompat.fromHtml("<b>${currentHp}<br></b><font color='$a'>Life </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtDefense.text = HtmlCompat.fromHtml("<b>${baseHero.baseDef}<br></b><font color='$a'>Defense </font>", HtmlCompat.FROM_HTML_MODE_LEGACY)

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