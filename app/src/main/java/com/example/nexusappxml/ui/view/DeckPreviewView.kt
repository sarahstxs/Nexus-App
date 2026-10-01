package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.nexusappxml.R

class ItemAlbumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imgHero: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.item_album, this, true)

        layoutParams = MarginLayoutParams(
            MarginLayoutParams.WRAP_CONTENT,
            MarginLayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 8, 8, 8)
        }

        imgHero = findViewById(R.id.imgAlbum)
    }

    fun bind(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.shadow_perfil_icon)
            .centerCrop()
            .into(imgHero)
    }
}