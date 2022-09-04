package org.seemeet.seemeet.ui.splash

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.login.RequestKakaoLogin
import org.seemeet.seemeet.ui.main.MainActivity
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.seemeet.seemeet.R.layout.activity_splash)
        var keyHash = Utility.getKeyHash(this)
        Log.d("kakaoHash", keyHash)
        checkRefreshApi()
        moveMain(1);
    }

    private fun moveMain(sec: Int) {
        Handler().postDelayed(Runnable {
            MainActivity.start(this)
            finish()
        }, 1000 * sec.toLong()) // sec초 정도 딜레이를 준 후 시작
    }

    private fun checkRefreshApi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("CheckRefreshApiError0", SeeMeetSharedPreference.getToken())
                Log.d("CheckRefreshApiError00", SeeMeetSharedPreference.getRefreshToken())
                val body = RetrofitBuilder.loginService.postRefreshToken(
                    SeeMeetSharedPreference.getToken(),
                    SeeMeetSharedPreference.getRefreshToken()
                )
                if(body.success){
                    Log.d("CheckRefreshApiError1", "1")
                    Log.d("CheckRefreshApiError2", body.data.accessToken)
                    Log.d("CheckRefreshApiError3", body.data.refreshToken)
                    SeeMeetSharedPreference.setToken(body.data.accessToken,body.data.refreshToken)
                }else{
                    Log.d("CheckRefreshApiError4", "2")
                    SeeMeetSharedPreference.clearStorage()
                }
            } catch (e: Exception) {
                Log.d("CheckRefreshApiError5", e.toString())
                SeeMeetSharedPreference.clearStorage()
            }
        }
    }
}