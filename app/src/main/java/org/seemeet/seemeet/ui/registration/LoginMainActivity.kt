package org.seemeet.seemeet.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.LoginService
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.login.RequestKakaoLogin
import org.seemeet.seemeet.databinding.ActivityLoginMainBinding
import org.seemeet.seemeet.ui.main.MainActivity


class LoginMainActivity : AppCompatActivity() {
    private val binding: ActivityLoginMainBinding by lazy {
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    //kakao
    private val userApiClient = UserApiClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
    }

    fun initClickListener() {
        binding.kakaoLogo.setOnClickListener {
            getKaKaoToken()
        }

        binding.tvEmailRegister.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
            //finish()
        }

        binding.tvEmailLogin.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
            //finish()
        }
    }

    private fun getKaKaoToken() {
        if (userApiClient.isKakaoTalkLoginAvailable(this)) {
            Log.d("kakaoLogin", "카카오톡으로 로그인 가능")
            userApiClient.loginWithKakaoTalk(
                this,
                callback = kakaoLoginCallback
            )
        } else {
            Log.d("kakaoLogin", "카카오톡으로 로그인 불가능")
            userApiClient.loginWithKakaoAccount(
                this,
                callback = kakaoLoginCallback
            )
        }
    }

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null) {
            Log.d("kakaoLogin", "카카오계정 로그인 성공 ${token.accessToken}")
            trySeeMeetSocialLogin(token.accessToken)
            // 사용자 정보 가져오기
            userApiClient.me { user, error ->
                if (error != null) {
                    Log.d("kakaoLogin", "카카오계정 사용자 정보 가져오기 실패")
                } else if (user != null) {
                    user.id
                    Log.d(
                        "kakaoLogin",
                        "카카오계정 사용자 정보 가져오기 성공\n" +
                                "닉네임 = ${user.kakaoAccount?.profile?.nickname}\n " +
                                "프사 : ${user.kakaoAccount?.profile?.profileImageUrl}\n" +
                                "이메일 : ${user.kakaoAccount?.email}\n" +
                                "아이디 : ${user.id}\n" +
                                "이름 : ${user.kakaoAccount?.name}"
                    )
                }
            }
        } else if (error != null) {
            Log.d("kakaoLogin", error.toString())
            Toast.makeText(this, "다시 로그인해주세요", Toast.LENGTH_LONG).show()
        }
    }

    private fun trySeeMeetSocialLogin(socialToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = RetrofitBuilder.loginService.postKakaoLogin(
                    RequestKakaoLogin(
                        socialToken,
                        "kakao"
                    )
                )
                SeeMeetSharedPreference.setToken(body.data.accessToken.accessToken)
                // 이름,닉네임 입력 안했으면 입력화면으로 이동, 했으면 메인으로 이동
                if (body.data.user.nickname.isNullOrEmpty()) {
                    RegisterNameIdActivity.start(this@LoginMainActivity)
                } else {
                    MainActivity.start(this@LoginMainActivity)
                }
            } catch (e: Exception) {
                Log.e("network error", e.toString())
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginMainActivity::class.java)
            context.startActivity(intent)
        }
    }
}