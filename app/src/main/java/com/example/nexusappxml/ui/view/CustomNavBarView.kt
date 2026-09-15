package com.example.nexusappxml.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nexusappxml.R;

public class CustomNavBarView extends LinearLayout {

    // Criando o Enum para as abas
    public enum Aba {
        BUY,
        COLLECTION,
        BATTLE,
        PODIUM,
        WHO

    }

    private TextView btnBattle;
    private TextView btnCollection;
    private TextView btnBuy;
    private TextView btnPodium;
    private TextView btnWho;

    public CustomNavBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_custom_nav_bar, this, true);

        // Mapeando os elementos do XML
        btnBattle = findViewById(R.id.btn_battle);
        btnCollection = findViewById(R.id.btn_collection);
        btnBuy = findViewById(R.id.btn_buy);
        btnPodium = findViewById(R.id.btn_podium);
        btnWho = findViewById(R.id.btn_who);
    }

    public void setAbaAtiva(Aba abaAtual) {
        // Reseta todos para a cor inativa
        int inactiveColor = Color.parseColor("#FFFFFF");

        if (btnBattle != null) btnBattle.setTextColor(inactiveColor);
        if (btnCollection != null) btnCollection.setTextColor(inactiveColor);
        if (btnBuy != null) btnBuy.setTextColor(inactiveColor);
        if (btnPodium != null) btnPodium.setTextColor(inactiveColor);
        if (btnWho != null) btnWho.setTextColor(inactiveColor);

        // Destaca apenas a aba atual
        int activeColor = Color.parseColor("#005f73");

        switch (abaAtual) {
            case BATTLE:
                if (btnBattle != null) btnBattle.setTextColor(activeColor);
                break;
            case BUY:
                if (btnBuy != null) btnBuy.setTextColor(activeColor);
                break;
            case COLLECTION:
                if (btnCollection != null) btnCollection.setTextColor(activeColor);
                break;
            case PODIUM:
                if (btnPodium != null) btnPodium.setTextColor(activeColor);
                break;
            case WHO:
                if (btnWho != null) btnWho.setTextColor(activeColor);
                break;
        }
    }
}