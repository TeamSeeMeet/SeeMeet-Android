package org.seemeet.seemeet.ui.registration

import android.graphics.Rect
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.ui.main.MainActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityRegisterBinding
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

    fun initClickListener() {
        binding.btnRegister.setOnClickListener {
            MainActivity.start(this)
        }

        binding.ivRegisterBack.setOnClickListener {
            finish()
        }
        binding.etName.addTextChangedListener{
            if (isNullorBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }
        binding.etEmailRegister.addTextChangedListener{
            if (pattern.matcher(binding.etEmailRegister.text).matches()) {
                //이메일 맞음
                binding.tvWarningEmail.visibility = View.INVISIBLE
                //이미 있을 경우if(){
                //      binding.tvWarningEmail.setText("이미 등록된 이메일이에요.")
                //      binding.tvWarningEmail.visibility = View.VISIBLE
                // }else binding.tvWarningEmail.visibility = View.INVISIBLE

            } else {
                //이메일 아님
                binding.tvWarningEmail.setText("올바른 이메일을 입력해주세요")
                binding.tvWarningEmail.visibility = View.VISIBLE
            }
            if (isNullorBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
            if(binding.etEmailRegister.text.isNullOrBlank())
                binding.tvWarningEmail.visibility = View.INVISIBLE
        }

        binding.etPw.addTextChangedListener {
            if (binding.etPw.text.length < 8) {
                binding.tvWarningPw.setText("8자 이상의 비밀번호를 입력해주세요.")
                binding.tvWarningPw.visibility = View.VISIBLE
            } else { //8자 이상인 경우
                if (!isPasswordFormat(binding.etPw.text.toString())) {//영문, 숫자 , 특수문자 중 2가지 이상 사용안했을 경우
                    binding.tvWarningPw.setText("영문, 숫자, 특수문자 중 2가지 이상을 사용해주세요.")
                    binding.tvWarningPw.visibility = View.VISIBLE
                }else binding.tvWarningPw.visibility = View.INVISIBLE
            }
            if(binding.etPw.text.isNullOrBlank())
                binding.tvWarningPw.visibility = View.INVISIBLE
            if (isNullorBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }

        binding.etCheckpw.addTextChangedListener{
            if(!binding.etCheckpw.text.toString().equals(binding.etPw.text.toString())){
                binding.tvWarningCheckpw.setText("비밀번호가 일치하지 않아요.")
                binding.tvWarningCheckpw.visibility = View.VISIBLE
            }else binding.tvWarningCheckpw.visibility= View.INVISIBLE //일치할 경우 tv 안 뜨게
            if(binding.etCheckpw.text.isNullOrBlank())binding.tvWarningCheckpw.visibility= View.INVISIBLE
            if (isNullorBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }
    }

    fun isPasswordFormat(password: String): Boolean {
        return password.matches("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,16}\$".toRegex())
    }

    private fun activeBtn() {
        binding.btnRegister.setBackgroundResource(R.drawable.rectangle_pink_10)
        binding.btnRegister.isClickable = true // 버튼 클릭할수 있게
        binding.btnRegister.isEnabled = true // 버튼 활성화
    }

    private fun unactiveBtn() {
        binding.btnRegister.setBackgroundResource(R.drawable.rectangle_gray_radius_10)
        binding.btnRegister.isClickable = false // 버튼 클릭할수 없게
        binding.btnRegister.isEnabled = false // 버튼 비활성화
    }

    private fun isNullorBlank(): Boolean { //하나라도 성립하면 true 반환 (= 4개 중에 하나라도 이상한게 있을 때)
        return binding.etName.text.isNullOrBlank() || binding.tvWarningEmail.isVisible || binding.tvWarningCheckpw.isVisible || binding.tvWarningPw.isVisible || binding.etEmailRegister.text.isNullOrBlank() || binding.etPw.text.isNullOrBlank() || binding.etCheckpw.text.isNullOrBlank()
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

}