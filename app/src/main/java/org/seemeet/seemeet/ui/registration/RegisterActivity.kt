package org.seemeet.seemeet.ui.registration

import android.graphics.Rect
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityRegisterBinding
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.viewmodel.RegisterViewModel
import org.seemeet.seemeet.util.activeBtn
import org.seemeet.seemeet.util.inactiveBtn
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private val pattern: Pattern = Patterns.EMAIL_ADDRESS
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
        statusObserver()
    }

    private fun statusObserver() {
        viewModel.status.observe(this, Observer { status ->
            if (status) {
                MainActivity.start(this@RegisterActivity)
            } else {
                binding.etEmailRegister.requestFocus()
                binding.tvWarningEmail.text = resources.getString(R.string.register_registeredEmail)
                binding.tvWarningEmail.makeVisible()
            }
        })
    }

    fun initClickListener() {
        binding.btnRegister.setOnClickListener {
            viewModel.requestRegisterList(
                binding.etName.text.toString(),
                binding.etEmailRegister.text.toString(),
                binding.etPw.text.toString(),
                binding.etCheckpw.text.toString()
            )
        }

        binding.ivRegisterBack.setOnClickListener {
            finish()
        }

        binding.etName.addTextChangedListener {
            if (isNullOrBlank()) {
                binding.btnRegister.inactiveBtn(R.drawable.rectangle_gray04_10)
            } else {
                binding.btnRegister.activeBtn()
            }
        }

        binding.etEmailRegister.addTextChangedListener {
            if (pattern.matcher(binding.etEmailRegister.text).matches()) {
                //이메일 맞음
                binding.tvWarningEmail.makeInVisible()
            } else {
                //이메일 아님
                binding.tvWarningEmail.text = resources.getString(R.string.register_incorrectEmail)
                binding.tvWarningEmail.makeVisible()
            }
            if (isNullOrBlank()) {
                binding.btnRegister.inactiveBtn(R.drawable.rectangle_gray04_10)
            } else {
                binding.btnRegister.activeBtn()
            }
            if (binding.etEmailRegister.text.isNullOrBlank())
                binding.tvWarningEmail.makeInVisible()
        }

        binding.etPw.addTextChangedListener {
            if (binding.etPw.text.length < 8) {
                binding.tvWarningPw.text = resources.getString(R.string.register_lengthPassword)
                binding.tvWarningPw.makeVisible()
            } else { //8자 이상인 경우
                if (!isPasswordFormat(binding.etPw.text.toString())) {//영문, 숫자 , 특수문자 중 2가지 이상 사용안했을 경우
                    binding.tvWarningPw.text = resources.getString(R.string.register_formatPassword)
                    binding.tvWarningPw.makeVisible()
                } else binding.tvWarningPw.makeInVisible()
            }
            if (binding.etPw.text.isNullOrBlank())
                binding.tvWarningPw.makeVisible()

            if (isNullOrBlank()) {
                binding.btnRegister.inactiveBtn(R.drawable.rectangle_gray04_10)
            } else {
                binding.btnRegister.activeBtn()
            }
        }

        binding.etCheckpw.addTextChangedListener {
            if (binding.etCheckpw.text.toString() != binding.etPw.text.toString()) {
                binding.tvWarningCheckpw.text =
                    resources.getString(R.string.register_incorrectPassword)
                binding.tvWarningCheckpw.makeVisible()
            } else binding.tvWarningCheckpw.makeInVisible() //일치할 경우 tv 안 뜨게

            if (binding.etCheckpw.text.isNullOrBlank())
                binding.tvWarningCheckpw.makeInVisible()

            if (isNullOrBlank()) {
                binding.btnRegister.inactiveBtn(R.drawable.rectangle_gray04_10)
            } else {
                binding.btnRegister.activeBtn()
            }
        }
    }

    private fun isPasswordFormat(password: String): Boolean {
        return password.matches(PASSWORD_FORMAT.toRegex())
    }

    private fun isNullOrBlank(): Boolean { //하나라도 성립하면 true 반환 (= 4개 중에 하나라도 이상한게 있을 때)
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