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

        // Vinculando os elementos de texto/botões de paginação do XML
        btnPrev = findViewById(R.id.btn_prev_page)
        btnNext = findViewById(R.id.btn_next_page)
        tvPage = findViewById(R.id.tv_page_number)

        // Como a rota /list-active traz todos os packs de uma vez, ocultamos os botões de paginação
        btnPrev.visibility = View.GONE
        btnNext.visibility = View.GONE

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        val userIdReal = TokenManager.getUserId(this)
        perfilPreview.loadDatas(userIdReal)
        coinsView.loadCoins(userIdReal)

        // 3. Configurando o RecyclerView com o Adapter e o clique no botão "Buy Pack"
        recyclerView.layoutManager = LinearLayoutManager(this)
        packAdapter = PackAdapter(emptyList()) { packSelecionado ->
            // Ação ao clicar no botão de comprar do pack
            realizarCompraPack(packSelecionado.id)
        }
        recyclerView.adapter = packAdapter

        // 5. Configuração da NavBar
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
                    val intent = Intent(this, CollectionActivity::class.java).apply {
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

        // 6. Chamando a nova API de packs ativos
        fetchPacks()
    }

    private fun fetchPacks() {
        tvPage.text = "Carregando packs..."
        tvPage.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getInstance(this@BuyActivity).getActivePacks()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val packResponse = response.body()!!
                        val listaPacks = packResponse.packs ?: emptyList()
                        packAdapter.updateData(listaPacks)
                        tvPage.text = "Packs Disponíveis (${listaPacks.size})"
                    } else {
                        Log.e("ERRO_API_PACKS", "Código do Erro: ${response.code()} - ${response.message()}")
                        Toast.makeText(this@BuyActivity, "Erro ao carregar packs", Toast.LENGTH_SHORT).show()
                        tvPage.text = "Falha ao carregar"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("API_ERROR", "Erro: ${e.message}")
                    Toast.makeText(this@BuyActivity, "Falha na conexão", Toast.LENGTH_SHORT).show()
                    tvPage.text = "Erro de conexão"
                }
            }
        }
    }

    // Função que aciona a rota de compra no backend
    private fun realizarCompraPack(packId: Int) {
        val userId = TokenManager.getUserId(this)
        if (userId == -1) {
            Toast.makeText(this, "Erro: Usuário não logado", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Processando compra...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Chama a rota /comprar-pack/{id_user}/{id_pack}
                val response = RetrofitClient.getInstance(this@BuyActivity).buyPack(userId, packId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val element = response.body()!!

                        if (element.isJsonArray) {
                            // Sucesso! O backend retornou a lista de heróis sorteados.
                            // Como 'element' já é o array JSON, transformamos ele em String diretamente:
                            val heroesJson = element.toString()

                            // Atualiza também as moedas no topo da tela após a compra
                            findViewById<CoinView>(R.id.coin_view).loadCoins(userId)

                            // Abre a tela de recompensa passando a string JSON
                            val intent = Intent(this@BuyActivity, PackRewardActivity::class.java).apply {
                                putExtra("WON_HEROES_JSON", heroesJson)
                            }
                            startActivity(intent)

                        } else if (element.isJsonObject) {
                            // Caso retorne a mensagem de erro (ex: moedas insuficientes)
                            val jsonObject = element.asJsonObject
                            val message = jsonObject.get("message")?.asString ?: "Erro na compra"
                            Toast.makeText(this@BuyActivity, message, Toast.LENGTH_LONG).show()
                            Log.d("APP_ERROR", message)
                        }
                    } else {
                        Toast.makeText(this@BuyActivity, "Erro ao realizar a compra", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BuyActivity, "Falha na conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.d("APP_ERROR", "${e.message}")
                }
            }
        }
    }
}