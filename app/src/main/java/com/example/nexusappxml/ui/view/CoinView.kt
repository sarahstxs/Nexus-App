package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nexusappxml.R
import com.example.nexusappxml.data.network.RetrofitClient // Adjust import if necessary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val txtCoins: TextView

    private val viewScope = CoroutineScope(Dispatchers.Main)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_coins, this, true)

        // Mapping XML elements
        txtCoins = findViewById(R.id.txtCoins)
    }

    fun loadCoins(userId: Int) {

        viewScope.launch {
            try {
                val apiService = RetrofitClient.getInstance(context)

                // Switches the request to a background thread (IO)
                val response = withContext(Dispatchers.IO) {
                    apiService.getUserDetails(userId)
                }

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        txtCoins.text = "${user.coins}"
                    }
                } else {
                    Log.e("API_PREVIEW", "API Error: ${response.code()}")
                    txtCoins.text = "Error"
                }
            } catch (e: Exception) {
                Log.e("API_PREVIEW", "Connection Error", e)
                txtCoins.text = "N/A"
            }
        }
    }
}