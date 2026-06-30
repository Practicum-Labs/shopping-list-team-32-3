package ru.practicum.shoppinglist.core.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class PreferencesService(private val context: Context) {

    val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    inline fun <reified T> put(key: String, value: T) {
        val editor = sharedPrefs.edit()

        if (value is String) {
            editor.putString(key, value)
            editor.apply()
            return
        }

        val jsonString = json.encodeToString(serializer<T>(), value)
        editor.putString(key, jsonString)
        editor.apply()
    }

    inline fun <reified T> get(key: String): T? {
        val jsonString = sharedPrefs.getString(key, null) ?: return null

        if (T::class == String::class) {
            @Suppress("UNCHECKED_CAST")
            return jsonString as T
        }

        return json.decodeFromString(serializer<T>(), jsonString)
    }

    fun remove(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }

    fun clear() {
        sharedPrefs.edit().clear().apply()
    }

    companion object {
        const val PREFS_NAME = "shoppinglists_preferences"
    }
}
