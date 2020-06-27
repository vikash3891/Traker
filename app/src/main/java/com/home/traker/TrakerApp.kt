package com.home.traker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.home.traker.utils.LocaleManagerMew

class TrakerApp : Application() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManagerMew.setLocale(base))
        //MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManagerMew.setLocale(this)
        //Log.d(MewConstants.mewLogs, "onConfigurationChanged: " + newConfig.locale.getLanguage())
    }
    companion object {
        @get:Synchronized
        var instance: TrakerApp? = null
            private set
        var appContext: Context? = null
            private set
    }
}
