package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.data.model.LoginRequest
import com.example.nexusappxml.data.network.RetrofitClient
import com.example.nexusappxml.ui.view.PerfilPreviewView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se já tem um token salvo antes de carregar a tela
        val tokenSaved = TokenManager.getToken(this)



        // Se o token existe, vai direto pra home
        if (!tokenSaved.isNullOrEmpty()) {
            GotoInitialPage()
            return
        }

        // Se não tem token, carrega a tela de login
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        // Esconde a barra de navegação
        controller.hide(WindowInsetsCompat.Type.navigationBars())

        // Faz com que a barra apareça apenas se o usuário arrastar de baixo para cima, e depois suma de novo
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        var imgBackground = findViewById<ImageView>(R.id.imgBackground)

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)


        // Botão para tela de registro
        val goToRegister = findViewById<Button>(R.id.buttonResgister)
        goToRegister.setOnClickListener { GotoRegister() }

        // Botão para logar
        val btnLogin = findViewById<TextView>(R.id.btnLogin)
        btnLogin.setOnClickListener { Enter() }
    }
    fun GotoRegister(){
        val intent = Intent(this, RegisterActivity2::class.java)
        startActivity(intent)

    }
    fun Enter() {
        val apiService = RetrofitClient.getInstance(this)

        lifecycleScope.launch {
            try {
                val formEmail = findViewById<EditText>(R.id.formEmail)
                val formPassword = findViewById<EditText>(R.id.formPassword)

                val loginDetails = LoginRequest(
                    email = formEmail.text.toString(),
                    password = formPassword.text.toString()
                )

                val awser = apiService.login(loginDetails)

                // Salva o token
                TokenManager.saveToken(this@MainActivity, awser.accessToken)

                // Salva o ID corretamente
                TokenManager.saveUserId(this@MainActivity, awser.userId)

                GotoInitialPage()

            } catch (e: retrofit2.HttpException) {
                Toast.makeText(this@MainActivity, "Incorrect Credentials!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("API_REGISTER", "$e")
                Toast.makeText(this@MainActivity, "Connection Error!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun GotoInitialPage() {
        val intent = Intent(this, InitialActivity::class.java)

        // Limpa o histórico de telas. O usuário não consegue voltar para o Login apertando "Voltar"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

}
