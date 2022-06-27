package org.seemeet.seemeet.ui.registration


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
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
import org.seemeet.seemeet.ui.mypage.MyPageActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.RegisterNameIdViewModel
import retrofit2.HttpException
import java.util.regex.Pattern

class RegisterNameIdActivity : AppCompatActivity() {
    private val binding: ActivityRegisterNameIdActivityBinding by lazy {
        ActivityRegisterNameIdActivityBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterNameIdViewModel by viewModels()
    var prev_etId: String? = SeeMeetSharedPreference.getUserId()
    var prev_etName: String? = SeeMeetSharedPreference.getUserName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.registerNameIdviewModel = viewModel
        binding.lifecycleOwner = this
        statusObserver()
        initClickListener()
    }

    fun inputCase(case: String, it: String, cursor_pos: Int) {
        // 이름에 입력 불가능한 문자를 입력했을 경우
        if (case == "name") {
            if (!MyPageActivity.isNameFormat(it[cursor_pos - 1].toString())) {
                viewModel.registerName.value = it.substring(
                    0,
                    cursor_pos - 1
                ) + it.substring(cursor_pos)
                viewModel.cursorPos.value = cursor_pos
                viewModel.invalidCase.value = true
            }
            prev_etName = it
        }
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

        viewModel.registerName.observe(this) {
            //그 직전 값이랑 입력값 비교해서 현재 커서 위치 알아내기
            var cursor_pos = it.length

            //입력한 경우
            if (it.length > 0 && prev_etName!!.length < it.length) {
                for (i in 0..prev_etName!!.length - 1) {
                    if (prev_etName!![i].lowercase() != it[i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                //입력불가 문자 입력한 경우
                inputCase("name", it, cursor_pos)
            }

            //지운 경우(드래그 전체 선택 후 입력한 경우도 포함)
            if (it.length > 0 && prev_etName!!.length >= it.length) {
                for (i in 0..it.length - 1) {
                    if (it[i].toString() != prev_etName!![i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                //입력불가 문자 입력한 경우
                inputCase("name", it, cursor_pos)
            }
        }

        viewModel.registerId.observe(this, Observer {
            viewModel.check()
        })

        viewModel.registerNameIdList.observe(this, Observer {
            setSharedPreference(it.data)
        })

        viewModel.registerStatus.observe(this) {
            if (it) {
                MainActivity.start(this@RegisterNameIdActivity)
            }
        }
    }

    fun initClickListener() {
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

        binding.btnStart.setOnClickListener {
            viewModel.requestRegisterNameIdList(
                binding.etName.text.toString(),
                binding.etId.text.toString()
            )
        }
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
