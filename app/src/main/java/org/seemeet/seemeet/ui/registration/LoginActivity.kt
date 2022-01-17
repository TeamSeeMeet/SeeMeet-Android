package org.seemeet.seemeet.ui.registration

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityLoginBinding
import org.seemeet.seemeet.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()

        var checkPWValue: Int = 0

        binding.etPw.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.ivPwShowHidden.visibility = View.VISIBLE

            } else {
                if (binding.etPw.text.isNullOrBlank()) {
                    binding.ivPwShowHidden.visibility = View.INVISIBLE
                } else binding.ivPwShowHidden.visibility = View.VISIBLE
            }
        }
        binding.ivPwShowHidden.setOnClickListener {
            if (checkPWValue == 0) {
                checkPWValue = 1
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_show)
                binding.etPw.transformationMethod = null
            } else {
                checkPWValue = 0
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_hidden)
                binding.etPw.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    fun initClickListener() {
        binding.btnLogin.setOnClickListener {//버튼 눌렀을 때 아이디 틀림, 비번 틀림
            /*
            if (!binding.etPw.text.equals("0000")) {
                CustomToast.createToast(this, "비밀번호가 틀렸습니다.")?.show()
            }
            if (!binding.etEmail.text.equals("hi@")) {
                CustomToast.createToast(this, "등록되지 않은 유저입니다.")?.show()
            }*/
            if (!binding.etPw.text.isNullOrBlank() && !binding.etEmail.text.isNullOrBlank()) {
                MainActivity.start(this)
            }
        }
        binding.tvRegister.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }

        //둘 다 채우면 로그인 버튼 색깔 바뀌게
        binding.etEmail.addTextChangedListener {
            if (!isNullorBlank()) { //다 작성했을 때
                activeBtn()
            } else unactiveBtn()
        }
        binding.etPw.addTextChangedListener {
            if (!isNullorBlank()) { //다 작성했을 때
                activeBtn()
            } else unactiveBtn()
        }
    }

    private fun isNullorBlank(): Boolean {
        return binding.etPw.text.isNullOrBlank() || binding.etEmail.text.isNullOrBlank()
    }

    private fun activeBtn() {
        binding.btnLogin.setBackgroundResource(R.drawable.rectangle_pink_10)
        binding.btnLogin.isClickable = true // 버튼 클릭할수 있게
        binding.btnLogin.isEnabled = true // 버튼 활성화
    }

    private fun unactiveBtn() {
        binding.btnLogin.setBackgroundResource(R.drawable.rectangle_gray04_10)
        binding.btnLogin.isClickable = false // 버튼 클릭할수 없게
        binding.btnLogin.isEnabled = false // 버튼 비활성화
    }
}