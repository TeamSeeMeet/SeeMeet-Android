package org.seemeet.seemeet.ui.registration


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.model.response.login.ExUser
import org.seemeet.seemeet.databinding.ActivityRegisterNameIdActivityBinding
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.RegisterNameIdViewModel
import retrofit2.HttpException
import java.util.regex.Pattern

class RegisterNameIdActivity : AppCompatActivity() {
    private val binding: ActivityRegisterNameIdActivityBinding by lazy {
        ActivityRegisterNameIdActivityBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterNameIdViewModel by viewModels()
    var prev_etId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.registerNameIdviewModel = viewModel
        binding.lifecycleOwner = this
        statusObserver()
        initClickListener()
    }

    private fun statusObserver() {
        viewModel.fetchState.observe(this) {
            var message = ""
            when (it.second) {
                BaseViewModel.FetchState.BAD_INTERNET -> {
                    message = "소켓 오류 / 서버와 연결에 실패하였습니다."
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val error = (it.first as HttpException)
                    if (error.response()!!.errorBody()!!.string()
                            .split("\"")[7] == "이미 사용중인 닉네임입니다."
                    ) {
                        binding.tvWarningId.text = "이미 사용 중이에요"
                    }
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    message = "호스트를 확인할 수 없습니다. 네트워크 연결을 확인해주세요"
                }
                else -> {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }
            }
            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())
            if (message != "") {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.registerId.observe(this, Observer {
            var cursor_pos = it.length
            if (it.length > 0 && prev_etId!!.length < it.length) {
                for (i in 0..prev_etId!!.length - 1) {
                    if (prev_etId!![i].lowercase() != it[i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                inputCase(it, cursor_pos)
            }
            if (it.length > 0 && prev_etId!!.length >= it.length) {
                for (i in 0..it.length - 1) {
                    if (it[i].toString() != prev_etId!![i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                inputCase(it, cursor_pos)
            }
            viewModel.check()
        })

        viewModel.registerNameIdList.observe(this, Observer {
            setSharedPreference(it.data)
        })

        viewModel.registerStatus.observe(this) {
            if (it) {
                val intent = Intent(this@RegisterNameIdActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //기존에 쌓여있던 액티비티를 삭제
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this@RegisterNameIdActivity.startActivity(intent)
            }
        }
    }

    fun initClickListener() {
        binding.ivRegisterBack.setOnClickListener {
            val intent = Intent(this@RegisterNameIdActivity, LoginMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.ivRegisterX.setOnClickListener {
            val intent = Intent(this@RegisterNameIdActivity, LoginMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.etName.setFilters(arrayOf(InputFilter { src, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (src.matches(Regex("^[ㄱ-ㅎ|가-힣|a-z|A-Z]*$"))) {
                    return@InputFilter src
                }
                return@InputFilter ""
            }
            null
        }, InputFilter.LengthFilter(5)))

        binding.etId.setFilters(arrayOf(InputFilter { src, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (src.matches(Regex("^[A-Za-z0-9._]*\$"))) {
                    return@InputFilter src
                }
                viewModel.tvWarningId.value = "아이디는 알파벳, 숫자, 밑줄, 마침표만 사용 가능해요"
                return@InputFilter ""
            }
            null
        }))

        binding.etId.setOnFocusChangeListener { _, hasFocus ->
            //포커스 나가면 불가능한 문자 입력했던 기록 없애기
            if (!hasFocus && viewModel.tvWarningId.value == "아이디는 알파벳, 숫자, 밑줄, 마침표만 사용 가능해요") {
                if (binding.etId.text.length < 7 && binding.etId.text.length > 0)
                    viewModel.tvWarningId.value = "7자 이상 써주세요"
                else viewModel.tvWarningId.value = ""
            }
        }

        binding.btnStart.setOnClickListener {
            viewModel.requestRegisterNameIdList(
                binding.etName.text.toString(),
                binding.etId.text.toString()
            )
        }
    }

    private fun inputCase(it: String, cursor_pos: Int) {
        // 아이디에 대문자를 입력했을 경우
        if (it[cursor_pos - 1] >= 'A' && it[cursor_pos - 1] <= 'Z') {
            viewModel.registerId.value = it.substring(
                0,
                cursor_pos - 1
            ) + it[cursor_pos - 1].lowercase() + it.substring(cursor_pos)
            viewModel.id_upperCase.value = true
            viewModel.id_cursorPos.value = cursor_pos
        }
        prev_etId = it
    }

    // sharedPreference setting
    private fun setSharedPreference(list: ExUser) {
        SeeMeetSharedPreference.setUserId(list.nickname ?: return)
        SeeMeetSharedPreference.setLogin(true)
        SeeMeetSharedPreference.setUserName(list.username)
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

        fun start(context: Context) {
            val intent = Intent(context, RegisterNameIdActivity::class.java)
            context.startActivity(intent)
        }
    }
}
