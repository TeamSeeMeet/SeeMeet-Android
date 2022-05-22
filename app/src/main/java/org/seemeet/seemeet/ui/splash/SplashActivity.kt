package org.seemeet.seemeet.ui.splash

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import org.seemeet.seemeet.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.seemeet.seemeet.R.layout.activity_splash)
        var keyHash = Utility.getKeyHash(this)
        Log.d("kakaoHash", keyHash)
        moveMain(1);
    }

    private fun moveMain(sec: Int) {
        Handler().postDelayed(Runnable {
            MainActivity.start(this)
            finish()
        }, 1000 * sec.toLong()) // sec초 정도 딜레이를 준 후 시작
    }
}