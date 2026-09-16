package com.example.nexusappxml.ui.activity

//import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.ui.view.CoinView
import com.example.nexusappxml.ui.view.CustomNavBarView
import com.example.nexusappxml.ui.view.GoBattleButton
import com.example.nexusappxml.ui.view.PerfilPreviewView


class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_initial)

        val perfilPreview = findViewById<PerfilPreviewView>(R.id.user_preview_component)
        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)
        val coinsView = findViewById<CoinView>(R.id.coin_view)
        val btnGoBattle = findViewById<GoBattleButton>(R.id.btn_go_battle)


        val userIdReal = TokenManager.getUserId(this)
        // Chame a função passando o ID do usuário logado
        perfilPreview.loadDatas(userIdReal)

        // Chame a função passando o ID do usuário logado
        coinsView.loadCoins(userIdReal)

        // Avisa a barra que o usuário está na tela de battle
        navBar.setAbaAtiva(CustomNavBarView.Aba.BATTLE)

        // Adiciona o botão para batalhar
        btnGoBattle.GotoBattle()

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