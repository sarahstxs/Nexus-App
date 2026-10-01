package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nexusappxml.R
import com.example.nexusappxml.data.network.RetrofitClient // Adjust import if necessary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilPreviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val txtUsername: TextView
    public val txtLevel: TextView

    private val viewScope = CoroutineScope(Dispatchers.Main)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_user_preview, this, true)

        // Mapping XML elements
        txtUsername = findViewById(R.id.txtUsername)
        txtLevel = findViewById(R.id.txtLevel)
    }

    fun loadDatas(userId: Int, onLoaded: (() -> Unit)? = null) {

        viewScope.launch {
            try {
                val apiService = RetrofitClient.getInstance(context)
                val response = withContext(Dispatchers.IO) {
                    apiService.getUserDetails(userId)
                }

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        txtUsername.text = user.username
                        txtLevel.text = "Level ${user.current_level}"

                        onLoaded?.invoke()
                    }
                } else {
                    Log.e("API_PREVIEW", "Error loading")
                    txtUsername.text = "Error loading"
                }
            } catch (e: Exception) {
                Log.e("API_PREVIEW", "Connection Error", e)
                txtUsername.text = "Disconnected"
            }
        }
    }
}