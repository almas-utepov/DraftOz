package kz.mobydev.draft.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
private const val SHARED_TOKEN = "SAVE_TOKEN"
private const val SHARED_LANGUAGE = "SAVE_LANGUAGE"
private const val SHARED_KEY = "DRAFT"
class PreferenceProvider(context: Context) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = appContext.getSharedPreferences(SHARED_KEY, Context.MODE_PRIVATE)

    fun clearShared(){
        preference.edit().clear().apply()
    }
    fun saveLanguage(language:String){
        preference.edit().putString(SHARED_LANGUAGE,language).apply()
    }
    fun getLanguage():String?{
        return preference.getString(SHARED_LANGUAGE, null)
    }

    fun saveToken(savedAt: String) {
        preference.edit().putString(SHARED_TOKEN, savedAt).apply()
    }

    fun getToken(): String? {
        return preference.getString(SHARED_TOKEN, null)
    }

    fun saveDarkModeEnabledState(enabled: Boolean) {
        preference.edit().putBoolean("DarkMode", enabled).apply()
    }

    fun getDarkModeEnabledState(): Boolean {
        return preference.getBoolean("DarkMode", false)
    }

}