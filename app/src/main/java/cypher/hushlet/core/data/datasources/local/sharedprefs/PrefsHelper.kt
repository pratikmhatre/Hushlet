package cypher.hushlet.core.data.datasources.local.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import cypher.hushlet.core.utils.AppConstants

class PrefsHelper(preferences: SharedPreferences) {
    companion object {
        private var INSTANCE: PrefsHelper? = null

        fun getPrefsHelper(context: Context): PrefsHelper {
            return INSTANCE ?: PrefsHelper(createPrefs(context = context)).also { INSTANCE = it }
        }

        private fun createPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(AppConstants.CARD_TABLE, Context.MODE_PRIVATE)
        }
    }
}