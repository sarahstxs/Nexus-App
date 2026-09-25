package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.network.RetrofitClient
import com.example.nexusappxml.ui.adapter.RankAdapter
import com.example.nexusappxml.ui.view.CustomNavBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PodiumActivity : AppCompatActivity() {

    private lateinit var rankAdapter: RankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_podium)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPodium)
        val imgBackground = findViewById<ImageView>(R.id.imgBackground)

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        // Configura o RecyclerView como lista vertical de faixas
        recyclerView.layoutManager = LinearLayoutManager(this)
        rankAdapter = RankAdapter(emptyList())
        recyclerView.adapter = rankAdapter

        navBar.setAbaAtiva(CustomNavBarView.Aba.PODIUM)

        // Configura a ação de clique vinda da barra de navegação
        navBar.onAbaSelectedListener = { aba ->
            when (aba) {
                CustomNavBarView.Aba.COLLECTION -> {
                    val intent = Intent(this, ChoseCollectionActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.BATTLE -> {
                    val intent = Intent(this, InitialActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.BUY -> {
                    val intent = Intent(this, BuyActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    }
                    startActivity(intent)
                }
                CustomNavBarView.Aba.WHO -> {
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        // Busca o ranking do servidor
        fetchRank()
    }

    private fun fetchRank() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getInstance(this@PodiumActivity).getRank()

                // LOG PARA DEBUGAR: Mostra o código HTTP e o erro do servidor, se houver
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string()
                Log.d("PodiumDebug", "Code: $errorCode, ErrorBody: $errorBody")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val rankResponse = response.body()!!
                        rankAdapter.updateData(rankResponse.ranking ?: emptyList())
                    } else {
                        // Mostra o código do erro no Toast para facilitar o teste visual
                        Toast.makeText(this@PodiumActivity, "Erro HTTP: $errorCode", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("PodiumDebug", "Exceção na requisição", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PodiumActivity, "Falha na conexão: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}