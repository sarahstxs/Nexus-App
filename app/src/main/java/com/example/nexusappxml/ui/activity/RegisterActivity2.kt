package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nexusappxml.R
import com.example.nexusappxml.data.model.RegisterRequest
import com.example.nexusappxml.data.network.ApiService
import com.example.nexusappxml.data.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime

class RegisterActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register2)

        // Botão para a tela de login
        val goToLogin = findViewById<Button>(R.id.btnGoToLogin)
        goToLogin.setOnClickListener { GotoLogin() }

        // Botão de cadastro
        val registerUSer = findViewById<TextView>(R.id.btnRegister)
        registerUSer.setOnClickListener {  RegisterUser()}
    }

    fun GotoLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun RegisterUser() {

        // Email
        val formEmail = findViewById<EditText>(R.id.formEmail)
        val txtEmail = formEmail.text.toString()
        // password
        val formPassword = findViewById<EditText>(R.id.formPassword)
        val txtPassword = formPassword.text.toString()
        // Username
        val formUsername = findViewById<EditText>(R.id.formUsername)
        val txtUsername = formUsername.text.toString()

        val txtError = findViewById<TextView>(R.id.txtErrorMessage)

        val request = RegisterRequest(
            "$txtUsername",
            "$txtPassword",
            "$txtEmail",
            false,
            true,
            1,
            300,
            LocalDateTime.now().toString(),
            1
        )
        lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getInstance(this@RegisterActivity2)

                val response = apiService.registerUser(request)

                if (response.isSuccessful) {
                    val successMsg = response.body()?.message
                    Log.d("API_REGISTER", "success $successMsg")
                    Toast.makeText(this@RegisterActivity2, "Registration successful!", Toast.LENGTH_SHORT).show()
                    GotoLogin()
                }
                else {
                    val errorJson = response.errorBody()?.string()

                    try {
                        val jsonObject = JSONObject(errorJson ?: "{}")
                        val cleanError = jsonObject.getString("detail")

                        txtError.text = cleanError
                    } catch (e: Exception) {
                        txtError.text = "An error occurred during registration."
                    }
                    txtError.visibility = View.VISIBLE
                }
            }
            catch (e: Exception){
                Log.e("API_REGISTER", "Connection error")
            }
        }
    }
}
