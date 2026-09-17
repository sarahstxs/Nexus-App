package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.nexusappxml.R

class ItemAlbumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var imgHero: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_deck_preview, this, true)
        imgHero = findViewById<ImageView>(R.id.imgHero)

        Glide.with(this)
            .load("https://comicvine.gamespot.com/a/uploads/original/11161/111612243/10012902-5140161970-b364e.jpg")
            .into(imgHero)
    }
}