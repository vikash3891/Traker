package com.home.traker.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleManagerMew {

    val SELECTED_LANGUAGE = "MEW_CURRENT_-- USER_LANGUAGE"

    var mEnglishFlag = "en"
    var mArabicFlag = "ar"
    var isArabicFlag = false

    fun setLocale(context: Context?): Context {
        return updateResources(context!!, getCurrentLanguage(context)!!)
    }

    inline fun setNewLocale(context: Context, language: String) {

        persistLanguagePreference(context, language)
        updateResources(context, language)
    }

    inline fun getCurrentLanguage(context: Context?): String? {

        var mCurrentLanguage: String?

        if (isArabicFlag)
            mCurrentLanguage = mArabicFlag
        else
            mCurrentLanguage = mEnglishFlag

        return mCurrentLanguage
    }

    fun persistLanguagePreference(context: Context, language: String) {
        isArabicFlag = language != mEnglishFlag

    }

    fun updateResources(context: Context, language: String): Context {

        var contextFun = context

        var locale = Locale(language)
        Locale.setDefault(locale)

        var resources = context.resources
        var configuration = Configuration(resources.configuration)

        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())

        return contextFun
    }
}