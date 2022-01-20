package org.seemeet.seemeet

import android.app.Application
import org.seemeet.seemeet.data.SeeMeetSharedPreference

class SeeMeetApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        SeeMeetSharedPreference.init(applicationContext)
    }
}