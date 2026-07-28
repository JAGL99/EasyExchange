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


    fun clear() {
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "MyAppPrefs"
    }
}
