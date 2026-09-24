package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class CollectionActivity : AppCompatActivity() {

    // 1. Declarando as variáveis de paginação e views aqui no topo
    private var currentPage = 1
    private val limitPerPage = 12
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var tvPage: TextView
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_collection)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val coinsView = findViewById<CoinView>(R.id.coin_view)
        var imgBackground = findViewById<ImageView>(R.id.imgBackground)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAlbuns)

        // 2. Vinculando os botões de paginação do XML
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

        // 3. Configurando o RecyclerView com o Adapter e o Clique!
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        albumAdapter = AlbumAdapter(emptyList()) { idClicado ->
            // Abre a tela da Ficha do Personagem quando clicar em uma imagem
            val intent = Intent(this@CollectionActivity, HeroProfileActivity::class.java)
            intent.putExtra("HEROI_ID", idClicado)
            startActivity(intent)
        }
        recyclerView.adapter = albumAdapter

        // 4. Configurando os cliques dos botões de página
        btnPrev.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                fetchHeroes(currentPage)
            }
        }

        btnNext.setOnClickListener {
            currentPage++
            fetchHeroes(currentPage)
        }

        // 5. Configuração da NavBar
        navBar.setAbaAtiva(CustomNavBarView.Aba.COLLECTION)
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
                CustomNavBarView.Aba.BUY -> {
                    val intent = Intent(this, BuyActivity::class.java).apply {
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
                CustomNavBarView.Aba.COLLECTION -> {
                    val intent = Intent(this, ChoseCollectionActivity::class.java).apply {
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

        // 6. Chamando a API pela primeira vez quando a tela abre!
        fetchHeroes(currentPage)
    }

    private fun fetchHeroes(page: Int) {
        tvPage.text = "Carregando..."
        btnPrev.isEnabled = false
        btnNext.isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Passa a página atual e o limite (12)
                val response = RetrofitClient.getInstance(this@CollectionActivity).getHeroes(page = page, limit = limitPerPage)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val heroResponse = response.body()!!

                        // CORREÇÃO: Usando o operador Elvis para evitar que a variável seja nula
                        val herois = heroResponse.heroes ?: emptyList()
                        albumAdapter.updateData(herois)

                        // Atualiza o texto da página
                        tvPage.text = "Página $page"

                        // Regras dos botões de paginação
                        btnPrev.isEnabled = page > 1
                        btnNext.isEnabled = herois.size == limitPerPage
                    } else {
                        Log.e("ERRO_API_HEROIS", "Código do Erro: ${response.code()} - ${response.message()}")
                        Toast.makeText(this@CollectionActivity, "Erro ao carregar heróis", Toast.LENGTH_SHORT).show()
                        tvPage.text = "Página $page"
                        btnPrev.isEnabled = page > 1
                        btnNext.isEnabled = true
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("API_ERROR", "Erro: ${e.message}")
                    Toast.makeText(this@CollectionActivity, "Falha na conexão", Toast.LENGTH_SHORT).show()
                    tvPage.text = "Página $page"
                    btnPrev.isEnabled = page > 1
                    btnNext.isEnabled = true
                }
            }
        }
    }
}