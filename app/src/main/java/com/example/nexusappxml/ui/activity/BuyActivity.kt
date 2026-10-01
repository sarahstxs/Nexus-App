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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.data.network.RetrofitClient
import com.example.nexusappxml.ui.adapter.PackAdapter
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.PerfilPreviewView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuyActivity : AppCompatActivity() {

    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var tvPage: TextView
    private lateinit var packAdapter: PackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_collection)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val coinsView = findViewById<CoinView>(R.id.coin_view)
        val imgBackground = findViewById<ImageView>(R.id.imgBackground)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAlbuns)

        btnPrev = findViewById(R.id.btn_prev_page)
        btnNext = findViewById(R.id.btn_next_page)
        tvPage = findViewById(R.id.tv_page_number)

        btnPrev.visibility = View.GONE
        btnNext.visibility = View.GONE

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        val userIdReal = TokenManager.getUserId(this)
        perfilPreview.loadDatas(userIdReal)
        coinsView.loadCoins(userIdReal)

        recyclerView.layoutManager = LinearLayoutManager(this)
        packAdapter = PackAdapter(emptyList()) { selectedPack ->
            buyPackAction(selectedPack.id)
        }
        recyclerView.adapter = packAdapter

        // 5. NavBar configuration
        navBar.setAbaAtiva(CustomNavBarView.Aba.BUY)
        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.BATTLE -> {
                    val intent = Intent(this, InitialActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.COLLECTION -> {
                    val intent = Intent(this, ChoseCollectionActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.PODIUM -> {
                    val intent = Intent(this, PodiumActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.WHO -> {
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        fetchPacks()
    }

    private fun fetchPacks() {
        tvPage.text = "Loading packs..."
        tvPage.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getInstance(this@BuyActivity).getActivePacks()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val packResponse = response.body()!!
                        val packList = packResponse.packs ?: emptyList()
                        packAdapter.updateData(packList)
                        tvPage.text = "Available Packs (${packList.size})"
                    } else {
                        Log.e("API_PACKS_ERROR", "Error Code: ${response.code()} - ${response.message()}")
                        Toast.makeText(this@BuyActivity, "Error loading packs", Toast.LENGTH_SHORT).show()
                        tvPage.text = "Failed to load"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("API_ERROR", "Error: ${e.message}")
                    Toast.makeText(this@BuyActivity, "Connection failed", Toast.LENGTH_SHORT).show()
                    tvPage.text = "Connection error"
                }
            }
        }
    }

    private fun buyPackAction(packId: Int) {
        val userId = TokenManager.getUserId(this)
        if (userId == -1) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Processing purchase...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Calls the route /comprar-pack/{id_user}/{id_pack}
                val response = RetrofitClient.getInstance(this@BuyActivity).buyPack(userId, packId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val element = response.body()!!

                        if (element.isJsonArray) {
                            val heroesJson = element.toString()

                            findViewById<CoinView>(R.id.coin_view).loadCoins(userId)

                            val intent = Intent(this@BuyActivity, PackRewardActivity::class.java).apply {
                                putExtra("WON_HEROES_JSON", heroesJson)
                            }
                            startActivity(intent)

                        } else if (element.isJsonObject) {
                            val jsonObject = element.asJsonObject
                            val message = jsonObject.get("message")?.asString ?: "Purchase error"
                            Toast.makeText(this@BuyActivity, message, Toast.LENGTH_LONG).show()
                            Log.d("APP_ERROR", message)
                        }
                    } else {
                        Toast.makeText(this@BuyActivity, "Error completing purchase", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BuyActivity, "Connection failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.d("APP_ERROR", "${e.message}")
                }
            }
        }
    }
}