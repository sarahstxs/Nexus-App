package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nexusappxml.R

class DeckPreviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var btnGoDeck: ConstraintLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.view_deck_preview, this, true)
        btnGoDeck = findViewById(R.id.btnGoDeck)
    }

    fun GotoDeckPage() {
        btnGoDeck.setOnClickListener {Log.d("RETURN", "Deck!!!")}
    }
}