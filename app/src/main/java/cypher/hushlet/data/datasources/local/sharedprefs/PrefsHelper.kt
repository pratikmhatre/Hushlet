package cypher.hushlet.data.datasources.local.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import cypher.hushlet.utils.AppConstants
import kotlinx.serialization.EncodeDefault

class PrefsHelper(preferences: SharedPreferences) {
    companion object {
        private var INSTANCE: PrefsHelper? = null

        fun getPrefsHelper(context: Context): PrefsHelper {
            return INSTANCE ?: PrefsHelper(createPrefs(context = context)).also { INSTANCE = it }
        }

        private fun createPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(AppConstants.CARDS_TABLE, Context.MODE_PRIVATE)
        }
    }
}