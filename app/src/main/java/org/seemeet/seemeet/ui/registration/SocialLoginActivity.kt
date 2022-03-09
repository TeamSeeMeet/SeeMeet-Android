package org.seemeet.seemeet.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import org.seemeet.seemeet.databinding.ActivitySocialLoginBinding

class SocialLoginActivity : AppCompatActivity() {
    private val binding: ActivitySocialLoginBinding by lazy {
        ActivitySocialLoginBinding.inflate(layoutInflater)
    }

    //kakao
    private val userApiClient = UserApiClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initClickListener()
    }

    private fun initClickListener() {
        binding.btnKakaoLogin.setOnClickListener {
            getKaKaoToken()
        }
    }

    private fun getKaKaoToken() {
        if (userApiClient.isKakaoTalkLoginAvailable(this)) {
            Log.d("mlog: kakaoLogin", "카카오톡으로 로그인 가능")
            userApiClient.loginWithKakaoTalk(
                this,
                callback = kakaoLoginCallback
            )
        } else {
            Log.d("mlog: kakaoLogin", "카카오톡으로 로그인 불가능")
            userApiClient.loginWithKakaoAccount(
                this,
                callback = kakaoLoginCallback
            )
        }
    }

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null) {
            Log.d("kakaoLogin", "카카오계정 로그인 성공 ${token.accessToken}")

            // 사용자 정보 가져오기
            userApiClient.me { user, error ->
                if (error != null) {
                    Log.d("kakaoLogin", "카카오계정 사용자 정보 가져오기 실패")
                } else if (user != null) {
                    Log.d(
                        "kakaoLogin",
                        "카카오계정 사용자 정보 가져오기 성공, " +
                                "닉네임 = ${user.kakaoAccount?.profile?.nickname}, " +
                                "프사 : ${user.kakaoAccount?.profile?.profileImageUrl}, " +
                                "이메일 : ${user.kakaoAccount?.email}"
                    )
                }
            }
        } else if (error != null) {
            Log.d("kakaoLogin", error.toString())
            Toast.makeText(this, "다시 로그인해주세요", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SocialLoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}