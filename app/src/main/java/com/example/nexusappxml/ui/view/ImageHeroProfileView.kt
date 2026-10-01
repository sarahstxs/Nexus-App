package com.example.nexusappxml.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.nexusappxml.R

class ImageHeroProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imgHero: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_image_hero_profile, this, true)


        // Finds the ImageView inside your layout
        imgHero = findViewById(R.id.imgHero)
    }

    fun bind(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.shadow_perfil_icon) // Optional: loading placeholder image
            .centerCrop()
            .into(imgHero)
    }
}