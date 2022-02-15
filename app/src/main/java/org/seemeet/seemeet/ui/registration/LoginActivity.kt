package org.seemeet.seemeet.ui.registration

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import okhttp3.ResponseBody
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.login.RequestLoginList
import org.seemeet.seemeet.data.model.response.login.ResponseErrorLoginList
import org.seemeet.seemeet.data.model.response.login.ResponseLoginList
import org.seemeet.seemeet.databinding.ActivityLoginBinding
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.util.*
import retrofit2.*


class LoginActivity : AppCompatActivity() {
    private var pwValue: Int = HIDDEN_PW

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
    }

    private fun initNetwork() {
        val requestLoginData = RequestLoginList(
            email = binding.etEmail.text.toString(),
            password = binding.etPw.text.toString()
        )
        val call: Call<ResponseLoginList> = RetrofitBuilder.loginService.postLogin(requestLoginData)

        call.enqueue(object : Callback<ResponseLoginList> {
            override fun onResponse(
                call: Call<ResponseLoginList>,
                response: Response<ResponseLoginList>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        SeeMeetSharedPreference.setToken(it.accesstoken)
                        SeeMeetSharedPreference.setUserId(it.user.id)
                        SeeMeetSharedPreference.setLogin(true)
                        SeeMeetSharedPreference.setUserName(it.user.username)
                        SeeMeetSharedPreference.setUserEmail(it.user.email)
                    }
                    MainActivity.start(this@LoginActivity)

                } else { //실패했을 때 두가지 경우 (이메일, 패스워드 틀림)
                    val errorBody: ResponseErrorLoginList? =
                        getLoginErrorResponse(response.errorBody()!!)

                    if (errorBody != null) {
                        CustomToast.createToast(this@LoginActivity, errorBody.message)?.show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLoginList>, t: Throwable) {
                Log.e("NetWorkTest", "error:$t")
            }
        })
    }

    fun initClickListener() {
        binding.btnLogin.setOnClickListener {
            initNetwork()
        }

        binding.tvRegister.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
            finish()
        }

        binding.ivPwShowHidden.setOnClickListener {
            if (pwValue == HIDDEN_PW) {
                pwValue = SHOW_PW
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_show)
                binding.etPw.transformationMethod = null
            } else {
                pwValue = HIDDEN_PW
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_hidden)
                binding.etPw.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //둘 다 채우면 로그인 버튼 색깔 바뀌게
        binding.etEmail.addTextChangedListener {
            if (!isNullOrBlank()) { //다 작성했을 때
                binding.btnLogin.activeBtn()
            } else binding.btnLogin.inactiveBtn(R.drawable.rectangle_gray04_10)
        }
        binding.etPw.addTextChangedListener {
            if (!isNullOrBlank()) { //다 작성했을 때
                binding.btnLogin.activeBtn()
            } else binding.btnLogin.inactiveBtn(R.drawable.rectangle_gray04_10)
        }

        binding.etPw.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ivPwShowHidden.makeVisible()

            } else {
                if (binding.etPw.text.isNullOrBlank()) {
                    binding.ivPwShowHidden.makeInVisible()
                } else binding.ivPwShowHidden.makeVisible()
            }
        }
    }

    private fun isNullOrBlank(): Boolean {
        return binding.etPw.text.isNullOrBlank() || binding.etEmail.text.isNullOrBlank()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        const val HIDDEN_PW = 0
        const val SHOW_PW = 1

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

        fun getLoginErrorResponse(errorBody: ResponseBody): ResponseErrorLoginList? {
            return RetrofitBuilder.seeMeetRetrofit.responseBodyConverter<ResponseErrorLoginList>(
                ResponseErrorLoginList::class.java,
                ResponseErrorLoginList::class.java.annotations
            ).convert(errorBody)
        }
    }
}