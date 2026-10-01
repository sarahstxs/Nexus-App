package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.data.model.HeroItemResponse
import com.example.nexusappxml.data.network.RetrofitClient
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.GoBattleButton
import com.example.nexusappxml.ui.view.PerfilPreviewView
import kotlinx.coroutines.launch

class InitialActivity : AppCompatActivity() {

    private lateinit var heroImageViews: List<ImageView>

    private val deckHeroes = MutableList<HeroItemResponse?>(6) { null }
    private var selectedSlotIndex: Int = -1

    private val selectHeroLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val heroId = result.data?.getIntExtra("EXTRA_HERO_ID", -1) ?: -1
            val heroImageUrl = result.data?.getStringExtra("EXTRA_HERO_IMAGE_URL") ?: ""

            if (heroId != -1 && selectedSlotIndex in 0..5) {

                val heroAlreadyInDeck = deckHeroes.any { it?.id == heroId }

                if (heroAlreadyInDeck) {
                    Toast.makeText(this, "Você não pode colocar um herói repetido no deck!", Toast.LENGTH_SHORT).show()
                } else {
                    deckHeroes[selectedSlotIndex] = HeroItemResponse(id = heroId, name = null, imageUrl = heroImageUrl)
                    updateDeckUI()

                    saveDeckToBackend()
                }
            }
        }
        selectedSlotIndex = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val imgBackground = findViewById<ImageView>(R.id.imgBackground)
        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val coinsView = findViewById<CoinView>(R.id.coin_view)
        val btnGoBattle = findViewById<GoBattleButton>(R.id.btn_go_battle)
        val btnCurrentLevel = findViewById<TextView>(R.id.currentLevel)

        heroImageViews = listOf(
            findViewById(R.id.imgHero1),
            findViewById(R.id.imgHero2),
            findViewById(R.id.imgHero3),
            findViewById(R.id.imgHero4),
            findViewById(R.id.imgHero5),
            findViewById(R.id.imgHero6)
        )

        val userIdReal = TokenManager.getUserId(this)

        perfilPreview.loadDatas(userId = userIdReal) {
            btnCurrentLevel.text = getString(R.string.level_format, perfilPreview.txtLevel.text)
        }

        coinsView.loadCoins(userId = userIdReal)

        loadUserDecks()

        setupHeroSlotClicks()

        navBar.setAbaAtiva(CustomNavBarView.Aba.BATTLE)
        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.COLLECTION -> {
                    val intent = Intent(this, ChoseCollectionActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.BUY -> {
                    val intent = Intent(this, BuyActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.PODIUM -> {
                    val intent = Intent(this, PodiumActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.WHO -> {
                    Toast.makeText(this@InitialActivity, "Em breve", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        btnGoBattle.setOnClickListener {
            if (deckHeroes.any { it == null }) {
                Toast.makeText(this, "Precisas de preencher todos os 6 espaços do deck antes de lutar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val heroIds = ArrayList(deckHeroes.map { it!!.id })
            val heroImages = ArrayList(deckHeroes.map { it!!.imageUrl ?: "" })

            val intent = Intent(this, BattleActivity::class.java).apply {
                putIntegerArrayListExtra("EXTRA_DECK_HERO_IDS", heroIds)
                putStringArrayListExtra("EXTRA_DECK_HERO_IMAGES", heroImages)
                putExtra("EXTRA_FLOOR", 1)    // Começa no andar 1 da torre
                putExtra("EXTRA_PLACE_ID", 1) // ID padrão do local
            }
            startActivity(intent)
        }

        perfilPreview.setOnClickListener { backToLogin() }
    }

    private fun setupHeroSlotClicks() {
        for (i in heroImageViews.indices) {
            heroImageViews[i].setOnClickListener {
                if (deckHeroes[i] != null) {
                    deckHeroes[i] = null
                    updateDeckUI()
                    Toast.makeText(this, "Herói removido do deck", Toast.LENGTH_SHORT).show()
                } else {
                    selectedSlotIndex = i
                    val intent = Intent(this, MyCollectionActivity::class.java).apply {
                        putExtra("SELECT_MODE", true)
                    }
                    selectHeroLauncher.launch(intent)
                }
            }
        }
    }

    private fun loadUserDecks() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getInstance(this@InitialActivity).listUserDecks()
                if (response.isSuccessful && response.body() != null) {
                    val userDecks = response.body()!!

                    if (userDecks.isNotEmpty()) {
                        val activeDeck = userDecks.first()
                        val apiHeroes = activeDeck.heroes ?: emptyList()

                        for (i in 0 until 6) {
                            deckHeroes[i] = if (i < apiHeroes.size) apiHeroes[i] else null
                        }
                        updateDeckUI()
                    }
                } else {
                    Toast.makeText(this@InitialActivity, "Erro ao carregar decks", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("InitialActivity", "Erro de ligação: ${e.message}")
            }
        }
    }

    private fun saveDeckToBackend() {
        if (deckHeroes.any { it == null }) return

        val h1 = deckHeroes[0]?.id ?: return
        val h2 = deckHeroes[1]?.id ?: return
        val h3 = deckHeroes[2]?.id ?: return
        val h4 = deckHeroes[3]?.id ?: return
        val h5 = deckHeroes[4]?.id ?: return
        val h6 = deckHeroes[5]?.id ?: return

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.getInstance(this@InitialActivity).saveDeck(h1, h2, h3, h4, h5, h6)
                if (response.isSuccessful) {
                    Toast.makeText(this@InitialActivity, "Deck guardado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@InitialActivity, "Erro ao guardar deck", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("InitialActivity", "Erro de ligação ao guardar: ${e.message}")
            }
        }
    }

    private fun updateDeckUI() {
        for (i in heroImageViews.indices) {
            val hero = deckHeroes[i]
            if (hero != null && !hero.imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(hero.imageUrl)
                    .centerCrop()
                    .into(heroImageViews[i])
            } else {
                heroImageViews[i].setImageDrawable(null)
                heroImageViews[i].setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
            }
        }
    }

    fun backToLogin() {
        TokenManager.clearToken(this@InitialActivity)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}