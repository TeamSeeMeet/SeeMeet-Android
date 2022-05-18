package org.seemeet.seemeet

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.v2.network.BuildConfig
import org.seemeet.seemeet.data.SeeMeetSharedPreference

class SeeMeetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, org.seemeet.seemeet.BuildConfig.KAKAO_APP_KEY)
        SeeMeetSharedPreference.init(applicationContext)
    }
}