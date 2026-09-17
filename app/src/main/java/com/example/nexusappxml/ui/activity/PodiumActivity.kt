package com.example.nexusappxml.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexusappxml.R
import com.example.nexusappxml.ui.view.CustomNavBarView

class PodiumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_podium)

        val navBar: CustomNavBarView = findViewById(R.id.nav_bar_customizada)

    }
}