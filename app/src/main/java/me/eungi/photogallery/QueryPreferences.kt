package me.eungi.photogallery

import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "photoGallery"
private const val PREF_SEARCH_QUERY = "searchQuery"
private const val PREF_LAST_RESULT_ID = "lastResultId"
private const val PREF_IS_POLLING = "isPolling"

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

    fun setSortedQuery(query: String) {
        prefs.edit().putString(PREF_SEARCH_QUERY, query).apply()
    }

    fun getLastResultId(): String {
        return prefs.getString(PREF_LAST_RESULT_ID, "")!!
    }

    fun setLastResultId(lastResultId: String) {
        prefs.edit().putString(PREF_LAST_RESULT_ID, lastResultId).apply()
    }

    fun isPolling(): Boolean {
        return prefs.getBoolean(PREF_IS_POLLING, false)
    }

    fun setPolling(isOn: Boolean) {
        prefs.edit().putBoolean(PREF_IS_POLLING, isOn).apply()
    }

}