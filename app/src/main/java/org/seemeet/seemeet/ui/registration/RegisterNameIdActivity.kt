package org.seemeet.seemeet.ui.registration

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityRegisterNameIdActivityBinding
import org.seemeet.seemeet.util.activeBtn
import org.seemeet.seemeet.util.inactiveBtn
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import java.util.regex.Pattern

class RegisterNameIdActivity : AppCompatActivity() {
    private val binding: ActivityRegisterNameIdActivityBinding by lazy {
        ActivityRegisterNameIdActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
    }

    fun initClickListener() {
        binding.ivRegisterBack.setOnClickListener {
            finish()
        }

        binding.ivRegisterX.setOnClickListener {
            finish()
        }

        binding.etName.addTextChangedListener {
            if (isNullOrBlank()) {
                binding.btnStart.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else {
                binding.btnStart.activeBtn()
            }
        }

        binding.etId.addTextChangedListener {
            if (binding.etId.text.isNullOrBlank())
                binding.tvWarningId.makeInVisible()
            if (!isIdFormat(binding.etId.text.toString())) { //불가능한 문자 입력했을 때
                val length = binding.etId.text.length
                binding.etId.setText(binding.etId.text.substring(0,length-1))
                binding.etId.setSelection(length-1)
                binding.tvWarningId.text = resources.getString(R.string.register_formatId)
                binding.tvWarningId.makeVisible()
            }
            else {
                val length = binding.etId.text.length
                if(length!=0) {
                    val it = binding.etId.text.toString().get(length - 1)
                    if (it >= 'A' && it <= 'Z') { //대문자 입력시 자동으로 소문자로 입력되게
                        binding.etId.setText(binding.etId.text.substring(0, length - 1))
                        binding.etId.setText(binding.etId.text.toString() + it.lowercase())
                        binding.etId.setSelection(length)
                    }
                    if (binding.etId.text.length < 7) {
                        binding.tvWarningId.text = resources.getString(R.string.register_lengthId)
                        binding.tvWarningId.makeVisible()
                    } else { //7자 이상인 경우
                        if (isNumberFormat(binding.etId.text.toString())) {//숫자로만 이루어진 경우
                            binding.tvWarningId.text =
                                resources.getString(R.string.register_numberId)
                            binding.tvWarningId.makeVisible()
                        } else if (is_Format(binding.etId.text.toString())) {//_로만 이루어진 경우
                            binding.tvWarningId.text = "_로만은 만들 수 없어요"
                            binding.tvWarningId.makeVisible()
                        } else if (isdotFormat(binding.etId.text.toString())) {//.로만 이루어진 경우
                            binding.tvWarningId.text ="마침표로만은 만들 수 없어요"
                            binding.tvWarningId.makeVisible()
                        }
                        else
                            binding.tvWarningId.makeInVisible()
                    }
                }
            }
            if (isNullOrBlank()) {
                binding.btnStart.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else {
                binding.btnStart.activeBtn()
            }
        }
        binding.btnStart.setOnClickListener {

        }
    }

    private fun isNumberFormat(password: String): Boolean {
        return Pattern.matches("^[0-9]*\$", password)
    }

    private fun isIdFormat(password: String): Boolean {
        return Pattern.matches("^[A-Za-z0-9._]*\$", password)
    }

    private fun is_Format(password: String): Boolean {
        return Pattern.matches("^[_]*\$", password)
    }

    private fun isdotFormat(password: String): Boolean {
        return Pattern.matches("^[.]*\$", password)
    }

    private fun isNullOrBlank(): Boolean { //하나라도 성립하면 true 반환
        return binding.tvWarningId.isVisible ||
                binding.etName.text.isNullOrBlank() ||
                binding.etId.text.isNullOrBlank()
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
