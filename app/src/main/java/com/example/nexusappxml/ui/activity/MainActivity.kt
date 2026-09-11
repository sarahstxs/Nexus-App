package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nexusappxml.R
import com.example.nexusappxml.data.local.TokenManager
import com.example.nexusappxml.data.model.LoginRequest
import com.example.nexusappxml.data.network.RetrofitClient
import kotlinx.coroutines.launch
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Verifica se já tem um token salvo antes de carregar a tela
        val tokenSaved = TokenManager.getToken(this)

        // Se o token existe
        if (!tokenSaved.isNullOrEmpty()) {
            GotoInitialPage()
            return
        }

        // Se não tem token
        setContentView(R.layout.activity_main)

        // Botão para tela de registro
        val goToRegister = findViewById<Button>(R.id.buttonResgister)
        goToRegister.setOnClickListener {GotoRegister()}

        // Botão para logar
        val btnLogin = findViewById<TextView>(R.id.btnLogin)
        btnLogin.setOnClickListener {Enter()}


    }
    fun GotoRegister(){
        val intent = Intent(this, RegisterActivity2::class.java)
        startActivity(intent)

    }

    fun Enter() {
        // Instancia o Retrofit passando o contexto da Activity (this)
        val apiService = RetrofitClient.getInstance(this)
        val txtErrorMessage = findViewById<TextView>(R.id.txtErrorMessage)

        // Chamadas de rede precisam rodar dentro de uma Coroutine no escopo da Activity
        lifecycleScope.launch {
            try {
                val formEmail = findViewById<EditText>(R.id.formEmail)
                val formPassword = findViewById<EditText>(R.id.formPassword)

                val txtEmail = formEmail.text.toString()
                val txtPassword = formPassword.text.toString()
                // Dados do schema de login
                val loginDetails = LoginRequest(
                    email = txtEmail,
                    password = txtPassword
                )

                // Faz a chamada POST enviando o objeto no corpo (body)
                val awser = apiService.login(loginDetails)

                // Salva o token usando 'this' (a Activity) como contexto
                TokenManager.saveToken(this@MainActivity, awser.accessToken)

                GotoInitialPage()

            } catch (e: retrofit2.HttpException) {
                txtErrorMessage.text = "incorrect credentials"
                txtErrorMessage.visibility = View.VISIBLE
            } catch (e: Exception) {
                txtErrorMessage.text = "Connection error"
                txtErrorMessage.visibility = View.VISIBLE
            }
        }
    }
        fun GotoInitialPage() {
            val intent = Intent(this, Initial::class.java)

            // Limpa o histórico de telas. O usuário não consegue voltar para o Login apertando "Voltar"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }

}
