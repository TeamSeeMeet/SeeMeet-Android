package org.seemeet.seemeet.ui.mypage

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityChangePwBinding
import org.seemeet.seemeet.ui.registration.LoginActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.ChangePWViewModel
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import retrofit2.HttpException

class ChangePwActivity : AppCompatActivity() {

    private var pwValue: Int = LoginActivity.HIDDEN_PW
    private val viewModel: ChangePWViewModel by viewModels()

    private val binding: ActivityChangePwBinding by lazy {
        ActivityChangePwBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.changePWviewModel = viewModel
        binding.lifecycleOwner = this
        initClickListener()
        statusObserver()
    }

    fun initClickListener() {
        binding.btnBacKChangePw.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            //비밀번호 변경
            if (viewModel.tvWarningPW.value == "" && viewModel.tvWarningCheckPW.value == "" && !binding.etNewpw.text.isNullOrBlank() && !binding.etCheckPw.text.isNullOrBlank()) {
//                viewModel.requestChangePW(
//                    binding.etNewpw.text.toString(),
//                    binding.etCheckPw.text.toString()
//                )
            }
        }

        binding.ivNewpwShowHidden.setOnClickListener {
            if (pwValue == LoginActivity.HIDDEN_PW) {
                pwValue = LoginActivity.SHOW_PW
                binding.ivNewpwShowHidden.setImageResource(R.drawable.ic_pw_show)
                binding.etNewpw.transformationMethod = null
                //커서 맨 뒤로
                binding.etNewpw.setSelection(binding.etNewpw.text.length)

            } else {
                pwValue = LoginActivity.HIDDEN_PW
                binding.ivNewpwShowHidden.setImageResource(R.drawable.ic_pw_hidden)
                binding.etNewpw.transformationMethod = PasswordTransformationMethod.getInstance()
                //커서 맨 뒤로
                binding.etNewpw.setSelection(binding.etNewpw.text.length)
            }
        }

        binding.ivCheckpwShowHidden.setOnClickListener {
            if (pwValue == LoginActivity.HIDDEN_PW) {
                pwValue = LoginActivity.SHOW_PW
                binding.ivCheckpwShowHidden.setImageResource(R.drawable.ic_pw_show)
                binding.etCheckPw.transformationMethod = null
                //커서 맨 뒤로
                binding.etCheckPw.setSelection(binding.etCheckPw.text.length)

            } else {
                pwValue = LoginActivity.HIDDEN_PW
                binding.ivCheckpwShowHidden.setImageResource(R.drawable.ic_pw_hidden)
                binding.etCheckPw.transformationMethod = PasswordTransformationMethod.getInstance()
                //커서 맨 뒤로
                binding.etCheckPw.setSelection(binding.etCheckPw.text.length)
            }
        }

        binding.etNewpw.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ivNewpwShowHidden.makeVisible()

            } else {
                if (binding.etNewpw.text.isNullOrBlank()) {
                    binding.ivNewpwShowHidden.makeInVisible()
                } else binding.ivNewpwShowHidden.makeVisible()
            }
        }

        binding.etCheckPw.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ivCheckpwShowHidden.makeVisible()

            } else {
                if (binding.etCheckPw.text.isNullOrBlank()) {
                    binding.ivCheckpwShowHidden.makeInVisible()
                } else binding.ivCheckpwShowHidden.makeVisible()
            }
        }
    }

    fun statusObserver() {
        viewModel.fetchState.observe(this) {
            var message = ""
            when (it.second) {
                BaseViewModel.FetchState.BAD_INTERNET -> {
                    //binding.clContent.visibility = View.INVISIBLE
                    //binding.clNetworkError.visibility = View.VISIBLE
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val error = (it.first as HttpException)
                    message = "${error.code()} ERROR : \n ${
                        error.response()!!.errorBody()!!.string().split("\"")[7]
                    }"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    //binding.clContent.visibility = View.INVISIBLE
                    //binding.clNetworkError.visibility = View.VISIBLE
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
        fun start(context: Context) {
            val intent = Intent(context, ChangePwActivity::class.java)
            context.startActivity(intent)
        }
    }
}