package com.doctoraak.doctoraakpatient.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.*


class MyContextWrapper(base: Context?) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context, language: String): ContextWrapper {
            var context = context
            val config = context.resources.configuration
            var sysLocale: Locale? = null
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                sysLocale = getSystemLocale(config)
            } else {
                sysLocale = getSystemLocaleLegacy(config)
            }
            if (language != "" && !sysLocale.getLanguage().equals(language)) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setSystemLocale(config, locale)
                } else {
                    setSystemLocaleLegacy(config, locale)
                }

            }
            if (Build.VERSION.SDK_INT >= 19)
                context = context.createConfigurationContext(config)

            return MyContextWrapper(context)
        }

        fun getSystemLocaleLegacy(config: Configuration): Locale {
            return config.locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun getSystemLocale(config: Configuration): Locale {
            return config.getLocales().get(0)
        }


        fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.locale = locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }
    }
}