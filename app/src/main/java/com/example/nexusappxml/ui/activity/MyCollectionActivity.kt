package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.data.network.RetrofitClient
import com.example.nexusappxml.ui.view.AlbumAdapter
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.PerfilPreviewView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCollectionActivity : AppCompatActivity() {

    private var currentPage = 1
    private val limitPerPage = 12
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var tvPage: TextView
    private lateinit var albumAdapter: AlbumAdapter

    private var currentHeroesList: List<Any> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_collection)

        val isSelectingForDeck = intent.getBooleanExtra("SELECT_MODE", false)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val coinsView = findViewById<CoinView>(R.id.coin_view)
        val imgBackground = findViewById<ImageView>(R.id.imgBackground)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAlbuns)

        btnPrev = findViewById(R.id.btn_prev_page)
        btnNext = findViewById(R.id.btn_next_page)
        tvPage = findViewById(R.id.tv_page_number)

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        val userIdReal = TokenManager.getUserId(this)

        perfilPreview.loadDatas(userIdReal)
        coinsView.loadCoins(userIdReal)

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        albumAdapter = AlbumAdapter(emptyList()) { clickedId ->
            if (isSelectingForDeck) {
                val clickedHero = currentHeroesList.find { hero ->
                    try {
                        val idField = hero.javaClass.getDeclaredField("id")
                        idField.isAccessible = true
                        (idField.get(hero) as? Int) == clickedId
                    } catch (e: Exception) {
                        false
                    }
                }

                val imageUrl = clickedHero?.let { getHeroImageUrl(it) } ?: ""

                // Returns the hero ID and image URL to InitialActivity
                val resultIntent = Intent().apply {
                    putExtra("EXTRA_HERO_ID", clickedId)
                    putExtra("EXTRA_HERO_IMAGE_URL", imageUrl)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                val intent = Intent(this, HeroProfileActivity::class.java)
                intent.putExtra("HERO_ID", clickedId)
                startActivity(intent)
            }
        }
        recyclerView.adapter = albumAdapter

        btnPrev.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                fetchHeroes(currentPage, userIdReal)
            }
        }

        btnNext.setOnClickListener {
            currentPage++
            fetchHeroes(currentPage, userIdReal)
        }

        navBar.setAbaAtiva(CustomNavBarView.Aba.COLLECTION)
        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.BATTLE -> {
                    val intent = Intent(this, InitialActivity::class.java).apply {
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
                CustomNavBarView.Aba.COLLECTION -> {
                    val intent = Intent(this, ChoseCollectionActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.WHO -> {
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        fetchHeroes(currentPage, userIdReal)
    }

    private fun fetchHeroes(page: Int, idUser: Int) {
        tvPage.text = "Loading..."
        btnPrev.isEnabled = false
        btnNext.isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getInstance(this@MyCollectionActivity).getUserHeroes(page = page, limit = limitPerPage, id_user = idUser)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val heroResponse = response.body()!!
                        val heroes = heroResponse.heroes ?: emptyList()

                        currentHeroesList = heroes as List<Any>
                        albumAdapter.updateData(heroes)

                        tvPage.text = "Page $page"
                        btnPrev.isEnabled = page > 1
                        btnNext.isEnabled = heroes.size == limitPerPage
                    } else {
                        Toast.makeText(this@MyCollectionActivity, "Error loading heroes", Toast.LENGTH_SHORT).show()
                        tvPage.text = "Page $page"
                        btnPrev.isEnabled = page > 1
                        btnNext.isEnabled = true
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MyCollectionActivity, "Connection failed", Toast.LENGTH_SHORT).show()
                    tvPage.text = "Page $page"
                    btnPrev.isEnabled = page > 1
                    btnNext.isEnabled = true
                }
            }
        }
    }

    private fun getHeroImageUrl(obj: Any): String {
        try {
            val clazz = obj.javaClass
            for (fieldName in listOf("imageUrl", "image", "photo", "url", "img", "image_url")) {
                try {
                    val field = clazz.getDeclaredField(fieldName)
                    field.isAccessible = true
                    val value = field.get(obj)
                    if (value is String) return value
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
        return ""
    }
}