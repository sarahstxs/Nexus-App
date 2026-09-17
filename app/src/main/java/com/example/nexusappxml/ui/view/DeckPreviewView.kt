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
        // Infla o seu XML exato (substitua 'view_deck_preview' pelo nome real do seu arquivo XML se for diferente)
        LayoutInflater.from(context).inflate(R.layout.view_deck_preview, this, true)

        // Ajusta os parâmetros para que o item caiba bem no RecyclerView / Grid
        layoutParams = MarginLayoutParams(
            MarginLayoutParams.WRAP_CONTENT,
            MarginLayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 8, 8, 8)
        }

        // Encontra o ImageView dentro do seu ConstraintLayout (btnGoDeck)
        imgHero = findViewById(R.id.imgHero)
    }

    // Função para carregar a imagem dinamicamente via Glide
    fun bind(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.shadow_perfil_icon) // Opcional: imagem de carregamento
            .into(imgHero)
    }
}