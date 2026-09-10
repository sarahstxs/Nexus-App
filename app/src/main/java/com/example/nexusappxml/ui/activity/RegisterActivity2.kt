package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexusappxml.R

class RegisterActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register2)

        val botao = findViewById<Button>(R.id.buttonLogin)

        // 2. Avisa o que deve acontecer quando ele for clicado
        botao.setOnClickListener {

            // 3. Cria a "Intenção" de sair desta tela (this) e ir para a SegundaActivity
            val intent = Intent(this, MainActivity::class.java)

            // 4. Dá a largada!
            startActivity(intent)

        }
    }
}