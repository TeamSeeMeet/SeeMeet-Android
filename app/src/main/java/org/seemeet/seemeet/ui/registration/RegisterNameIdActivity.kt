package org.seemeet.seemeet.ui.registration

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.seemeet.seemeet.databinding.ActivityRegisterNameIdActivityBinding
import org.seemeet.seemeet.ui.viewmodel.RegisterNameIdViewModel
import java.util.regex.Pattern

class RegisterNameIdActivity : AppCompatActivity() {
    private val binding: ActivityRegisterNameIdActivityBinding by lazy {
        ActivityRegisterNameIdActivityBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterNameIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.registerNameIdviewModel = viewModel
        binding.lifecycleOwner = this
        initClickListener()
    }

    fun initClickListener() {

        viewModel.registerId.observe(this, Observer {
            viewModel.check()
        })

        binding.ivRegisterBack.setOnClickListener {
            finish()
        }

        binding.ivRegisterX.setOnClickListener {
            finish()
        }

        binding.etId.setOnFocusChangeListener { _, hasFocus ->
            // 아이디 길이가 0이면서 warning에 불가능한 문자 입력했던 기록 있을 때 포커스 나가면 warning 없애기
            if (!hasFocus && viewModel.registerId.value?.length == 0 && viewModel.status.value == 1) {
                viewModel.tvWarningId.value = ""
            }
        }

//        binding.btnStart.setOnClickListener {
//
//        }
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
        fun isNumberFormat(password: String): Boolean {
            return Pattern.matches("^[0-9]*\$", password)
        }

        fun isIdFormat(password: String): Boolean {
            return Pattern.matches("^[A-Za-z0-9._]*\$", password)
        }

        fun is_Format(password: String): Boolean {
            return Pattern.matches("^[_]*\$", password)
        }

        fun isdotFormat(password: String): Boolean {
            return Pattern.matches("^[.]*\$", password)
        }
    }
}
