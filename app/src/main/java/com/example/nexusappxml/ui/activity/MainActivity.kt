package com.example.nexusappxml.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nexusappxml.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Encontra o botão pelo ID que demos no XML
        val botao = findViewById<Button>(R.id.buttonResgister)

        // 2. Avisa o que deve acontecer quando ele for clicado
        botao.setOnClickListener {

            // 3. Cria a "Intenção" de sair desta tela (this) e ir para a SegundaActivity
            val intent = Intent(this, RegisterActivity2::class.java)

            // 4. Dá a largada!
            startActivity(intent)

        }

    }
}