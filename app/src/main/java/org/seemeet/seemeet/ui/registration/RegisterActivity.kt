package org.seemeet.seemeet.ui.registration

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.register.RequestRegisterList
import org.seemeet.seemeet.data.model.response.register.ResponseRegisterList
import org.seemeet.seemeet.databinding.ActivityRegisterBinding
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
    }

    fun initNetwork() {
        val requestRegisterService = RequestRegisterList(
            username = binding.etName.text.toString(),
            email = binding.etEmailRegister.text.toString(),
            password = binding.etPw.text.toString(),
            passwordConfirm = binding.etCheckpw.text.toString()
        )
        val call: Call<ResponseRegisterList> =
            RetrofitBuilder.registerService.postRegister(requestRegisterService)

        call.enqueue(object : Callback<ResponseRegisterList> {
            override fun onResponse(
                call: Call<ResponseRegisterList>,
                response: Response<ResponseRegisterList>
            ) {
                if (response.isSuccessful) {
                    MainActivity.start(this@RegisterActivity)
                } else {
                    binding.etEmailRegister.requestFocus()
                    binding.tvWarningEmail.text = "@string/register_registeredEmail"
                    binding.tvWarningEmail.makeVisible()
                }
            }

            override fun onFailure(call: Call<ResponseRegisterList>, t: Throwable) {
                Log.e("NetWorkTest", "error:$t")
            }
        })
    }

    fun initClickListener() {
        binding.btnRegister.setOnClickListener {
            initNetwork()
        }

        binding.ivRegisterBack.setOnClickListener {
            Log.d("****************ivRegisterBack", "Clicked")
            finish()
        }

        binding.etName.addTextChangedListener {
            if (isNullorBlank()) {
                inactiveBtn()
            } else {
                activeBtn()
            }
        }
        binding.etEmailRegister.addTextChangedListener {
            if (pattern.matcher(binding.etEmailRegister.text).matches()) {
                //이메일 맞음
                binding.tvWarningEmail.makeInVisible()
            } else {
                //이메일 아님
                binding.tvWarningEmail.text = "@string/register_incorrectEmail"
                binding.tvWarningEmail.makeVisible()
            }
            if (isNullorBlank()) {
                inactiveBtn()
            } else {
                activeBtn()
            }
            if (binding.etEmailRegister.text.isNullOrBlank())
                binding.tvWarningEmail.makeInVisible()
        }

        binding.etPw.addTextChangedListener {
            if (binding.etPw.text.length < 8) {
                binding.tvWarningPw.text = "@string/register_lengthPassword"
                binding.tvWarningPw.makeVisible()
            } else { //8자 이상인 경우
                if (!isPasswordFormat(binding.etPw.text.toString())) {//영문, 숫자 , 특수문자 중 2가지 이상 사용안했을 경우
                    binding.tvWarningPw.text = "@string/register_formatPassword"
                    binding.tvWarningPw.makeVisible()
                } else binding.tvWarningPw.makeInVisible()
            }
            if (binding.etPw.text.isNullOrBlank())
                binding.tvWarningPw.makeVisible()
            if (isNullorBlank()) {
                inactiveBtn()
            } else {
                activeBtn()
            }
        }

        binding.etCheckpw.addTextChangedListener {
            if (!binding.etCheckpw.text.toString().equals(binding.etPw.text.toString())) {
                binding.tvWarningCheckpw.text = "@string/register_incorrectPassword"
                binding.tvWarningCheckpw.makeVisible()
            } else binding.tvWarningCheckpw.makeInVisible() //일치할 경우 tv 안 뜨게
            if (binding.etCheckpw.text.isNullOrBlank())
                binding.tvWarningCheckpw.makeInVisible()
            if (isNullorBlank()) {
                inactiveBtn()
            } else {
                activeBtn()
            }
        }
    }

    fun isPasswordFormat(password: String): Boolean {
        return password.matches(PASSWORD_FORMAT.toRegex())
    }

    private fun activeBtn() {
        binding.btnRegister.apply {
            setBackgroundResource(R.drawable.rectangle_pink_10)
            isClickable = true // 버튼 클릭할수 있게
            isEnabled = true // 버튼 활성화
        }
    }

    private fun inactiveBtn() {
        binding.btnRegister.apply {
            setBackgroundResource(R.drawable.rectangle_gray_radius_10)
            isClickable = false // 버튼 클릭할수 없게
            isEnabled = false // 버튼 비활성화
        }
    }

    private fun isNullorBlank(): Boolean { //하나라도 성립하면 true 반환 (= 4개 중에 하나라도 이상한게 있을 때)
        return binding.etName.text.isNullOrBlank() ||
                binding.tvWarningEmail.isVisible ||
                binding.tvWarningCheckpw.isVisible ||
                binding.tvWarningPw.isVisible ||
                binding.etEmailRegister.text.isNullOrBlank() ||
                binding.etPw.text.isNullOrBlank() ||
                binding.etCheckpw.text.isNullOrBlank()
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
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        const val PASSWORD_FORMAT =
            "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,16}\$"
    }
}