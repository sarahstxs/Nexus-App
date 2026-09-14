package com.example.nexusappxml.ui.activity

//import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.ui.view.CustomNavBarView


class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)


        // Avisa a barra que o usuário está na tela de battle
        navBar.setAbaAtiva(CustomNavBarView.Aba.BATTLE)

        // Botão de voltar
        val formUsernameEmail = findViewById<Button>(R.id.exit)
        formUsernameEmail.setOnClickListener {backToLogin()}

        }
    fun backToLogin() {
        TokenManager.clearToken(this@InitialActivity)

        val intent = Intent(this, MainActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
    }