package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nexusappxml.R

class GoBattleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var txtGoBattle: ConstraintLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.button_go_battle, this, true)
        txtGoBattle = findViewById(R.id.btnGoBattle)
    }

    fun GotoBattle() {
        txtGoBattle.setOnClickListener {Log.d("RETURN", "Go!!!")}
    }
}