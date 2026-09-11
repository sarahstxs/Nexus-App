package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nexusappxml.R
import com.example.nexusappxml.data.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register2)

        // Botão para a tela de login
        val goToLogin = findViewById<Button>(R.id.btnGoToLogin)
        goToLogin.setOnClickListener { GotoLogin() }
    }

    fun GotoLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
