package me.eungi.photogallery

import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "photoGallery"
private const val PREF_SEARCH_QUERY = "searchQuery"

object QueryPreferences {
    private var isInit = false
    private lateinit var prefs: SharedPreferences

    fun get(context: Context): QueryPreferences {
        return if (isInit) this
        else {
            isInit = true
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            this
        }
    }

    fun getStoredQuery(): String {
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun setSotredQuery(query: String) {
        prefs.edit().putString(PREF_SEARCH_QUERY, query).apply()
    }

}