package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager


class Initial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial)

        // Botão de voltar
        val formUsernameEmail = findViewById<Button>(R.id.exit)
        formUsernameEmail.setOnClickListener {backToLogin()}

        }
    fun backToLogin() {
        TokenManager.clearToken(this@Initial)

        val intent = Intent(this, MainActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
    }