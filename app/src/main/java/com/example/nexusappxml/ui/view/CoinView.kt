package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nexusappxml.R
import com.example.nexusappxml.data.network.RetrofitClient // Ajuste o import se necessário
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // 1. Declarar os elementos fora do init para serem acessíveis em toda a classe
    private val txtCoins: TextView

    // Criar um escopo de corrotina específico para rodar a chamada de rede
    private val viewScope = CoroutineScope(Dispatchers.Main)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_coins, this, true)

        // Mapeando os elementos do XML
        txtCoins = findViewById(R.id.txtCoins)
    }

    fun loadCoins(idDoUsuario: Int) {

        viewScope.launch {
            try {
                // Instancia o Retrofit passando o contexto da própria View
                val apiService = RetrofitClient.getInstance(context)

                // Joga a requisição para uma thread de background (IO)
                val response = withContext(Dispatchers.IO) {
                    apiService.getUserDetails(idDoUsuario)
                }

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Sucesso: Atualiza o texto na tela com os dados do FastAPI
                        txtCoins.text = "${user.coins}"
                    }
                } else {
                    Log.e("API_PREVIEW", "=API Error: ${response.code()}")
                    txtCoins.text = "Error"
                }
            } catch (e: Exception) {
                Log.e("API_PREVIEW", "Conection Error", e)
                txtCoins.text = "Desc."
            }
        }
    }
}