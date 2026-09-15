package com.example.nexusappxml.data.local

import android.content.Context

object TokenManager {
    private const val PREF_NAME = "app_prefs"
    private const val TOKEN_KEY = "jwt_token"
    // Ao logar
    fun saveToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    // Para validar se o usuário esta logado ou não
    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN_KEY, null)
    }

    // Para deslogar
    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        // Abre o editor, remove a chave específica e aplica a mudança
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .apply()
    }

    fun saveUserId(context: Context, id: Int) {
        val prefs = context.getSharedPreferences("prefs_nexus_app", Context.MODE_PRIVATE)
        prefs.edit().putInt("USER_ID", id).apply()
    }

    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences("prefs_nexus_app", Context.MODE_PRIVATE)
        return prefs.getInt("USER_ID", -1) // Retorna -1 se der erro ou não achar
    }
}