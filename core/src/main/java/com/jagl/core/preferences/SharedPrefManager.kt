package com.jagl.core.preferences

import android.content.Context
import android.content.SharedPreferences


class SharedPrefManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()


    fun saveString(key: String?, value: String?) {
        editor.putString(key, value)
        editor.apply()
    }


    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }


    fun saveInt(key: String?, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }


    fun getInt(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }


    fun remove(key: String?) {
        editor.remove(key)
        editor.apply()
    }


    fun clear() {
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "MyAppPrefs"
        private var instance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (instance == null) {
                instance = SharedPrefManager(context)
            }
            return instance!!
        }
    }
}
