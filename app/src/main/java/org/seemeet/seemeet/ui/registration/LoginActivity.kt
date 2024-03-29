package org.seemeet.seemeet.ui.registration

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.model.response.login.Data
import org.seemeet.seemeet.databinding.ActivityLoginBinding
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.LoginViewModel
import org.seemeet.seemeet.util.*
import retrofit2.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    private var pwValue: Int = HIDDEN_PW
    private val pattern: Pattern = Patterns.EMAIL_ADDRESS
    private val viewModel: LoginViewModel by viewModels()
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        statusObserver()
        initClickListener()
    }

    private fun statusObserver() {
        viewModel.loginList.observe(this, Observer { list ->

            if (list.data.user.username.isNullOrEmpty() && list.data.user.nickname.isNullOrEmpty()) {
                SeeMeetSharedPreference.setUserName("0")
            } else {
                SeeMeetSharedPreference.setUserName(list.data.user.username)
            }
            setSharedPreference(list.data)

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //기존에 쌓여있던 액티비티를 삭제
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this@LoginActivity.startActivity(intent)
        })

        viewModel.fetchState.observe(this) {
            var message = ""
            when (it.second) {
                BaseViewModel.FetchState.BAD_INTERNET -> {
                    binding.clContent.visibility = View.INVISIBLE
                    binding.clNetwork.visibility = View.VISIBLE
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val error = (it.first as HttpException)
                    message = "${error.response()!!.errorBody()!!.string().split("\"")[7]}"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    binding.clContent.visibility = View.INVISIBLE
                    binding.clNetwork.visibility = View.VISIBLE
                }
                else -> {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }
            }

            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())
            if (message != "") {
                CustomToast.createToast(this@LoginActivity, message)?.show()
            }
        }
    }

    fun initClickListener() {
        binding.ivX.setOnClickListener { finish() }

        binding.ivLoginBack.setOnClickListener { finish() }

        binding.btnLogin.setOnClickListener {
            viewModel.requestLoginList(
                binding.etEmail.text.toString(),
                binding.etPw.text.toString()
            )
        }

        binding.tvRegister.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }

        binding.ivPwShowHidden.setOnClickListener {
            if (pwValue == HIDDEN_PW) {
                pwValue = SHOW_PW
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_show)
                binding.etPw.transformationMethod = null
                //커서 맨 뒤로
                binding.etPw.setSelection(binding.etPw.text.length)

            } else {
                pwValue = HIDDEN_PW
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_hidden)
                binding.etPw.transformationMethod = PasswordTransformationMethod.getInstance()
                //커서 맨 뒤로
                binding.etPw.setSelection(binding.etPw.text.length)
            }
        }

        //둘 다 채우면 로그인 버튼 색깔 바뀌게
        binding.etEmail.addTextChangedListener {
            if (!isNullOrBlank()) { //다 작성했을 때
                binding.btnLogin.activeBtn()
            } else binding.btnLogin.inactiveBtn(R.drawable.rectangle_gray02_10)
        }
        binding.etPw.addTextChangedListener {
            if (!isNullOrBlank()) { //다 작성했을 때
                binding.btnLogin.activeBtn()
            } else binding.btnLogin.inactiveBtn(R.drawable.rectangle_gray02_10)
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

    private fun setSharedPreference(data: Data) {
        SeeMeetSharedPreference.setToken(data.accessToken, data.refreshToken)
        SeeMeetSharedPreference.setUserId(data.user.nickname ?: "")
        SeeMeetSharedPreference.setLogin(true)
        SeeMeetSharedPreference.setUserProfile(data.user.imgLink)
        SeeMeetSharedPreference.setPushOn(data.user.push)
    }

    private fun isNullOrBlank(): Boolean {
        return binding.etPw.text.isNullOrBlank() ||
                binding.etEmail.text.isNullOrBlank() ||
                !pattern.matcher(binding.etEmail.text).matches()
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
    }
}