package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        val tokenSaved = TokenManager.getToken(this)

        if (!tokenSaved.isNullOrEmpty()) {
            gotoInitialPage()
            return
        }

        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        controller.hide(WindowInsetsCompat.Type.navigationBars())

        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val imgBackground = findViewById<ImageView>(R.id.imgBackground)

        Glide.with(this)
            .load("https://i.pinimg.com/736x/a7/2a/02/a72a022f37c47d8d294e85577737f362.jpg")
            .centerCrop()
            .into(imgBackground)

        // Button for the registration screen
        val goToRegister = findViewById<Button>(R.id.buttonResgister)
        goToRegister.setOnClickListener { gotoRegister() }

        // Login button
        val btnLogin = findViewById<TextView>(R.id.btnLogin)
        btnLogin.setOnClickListener { enter() }
    }

    private fun gotoRegister() {
        val intent = Intent(this, RegisterActivity2::class.java)
        startActivity(intent)
    }

    private fun enter() {
        val apiService = RetrofitClient.getInstance(this)

        lifecycleScope.launch {
            try {
                val formEmail = findViewById<EditText>(R.id.formEmail)
                val formPassword = findViewById<EditText>(R.id.formPassword)

                val loginDetails = LoginRequest(
                    email = formEmail.text.toString(),
                    password = formPassword.text.toString()
                )

                val answer = apiService.login(loginDetails)

                Log.d("TOKEN_TEST", "Token received: ${answer.accessToken}")

                // Saves the token
                TokenManager.saveToken(this@MainActivity, answer.accessToken)

                // Saves the ID correctly
                TokenManager.saveUserId(this@MainActivity, answer.userId)

                gotoInitialPage()

            } catch (e: retrofit2.HttpException) {
                Toast.makeText(this@MainActivity, "Incorrect Credentials!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("API_LOGIN", "$e")
                Toast.makeText(this@MainActivity, "Connection Error!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun gotoInitialPage() {
        val intent = Intent(this, InitialActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}