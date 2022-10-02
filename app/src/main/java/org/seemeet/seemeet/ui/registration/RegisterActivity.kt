package org.seemeet.seemeet.ui.registration

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
import androidx.lifecycle.Observer
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.ActivityRegisterBinding
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.RegisterViewModel
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private var pwValue: Int = LoginActivity.HIDDEN_PW
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this
        initClickListener()
        statusObserver()
    }

    private fun statusObserver() {
        viewModel.registerList.observe(this, Observer { list ->
            SeeMeetSharedPreference.setToken(list.data.accessToken, list.data.refreshToken)
        })

        viewModel.status.observe(this, Observer { status ->
            if (status) {
                val intent = Intent(this, RegisterNameIdActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) //스택에 남지 않게
                intent.putExtra("NAME", "")
                startActivity(intent)
            } else {
                binding.etEmailRegister.requestFocus()
                binding.tvWarningEmail.text = resources.getString(R.string.register_registeredEmail)
            }
        })

        viewModel.fetchState.observe(this) {
            var message = ""
            when (it.second) {
                BaseViewModel.FetchState.BAD_INTERNET -> {
                    binding.clContent.makeInVisible()
                    binding.clNetworkError.makeVisible()
                    binding.clBottom.makeInVisible()
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val error = (it.first as HttpException)
                    message = "${error.code()} ERROR : \n ${
                        error.response()!!.errorBody()!!.string().split("\"")[7]
                    }"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    binding.clContent.makeInVisible()
                    binding.clNetworkError.makeVisible()
                    binding.clBottom.makeInVisible()
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

    fun initClickListener() {
        binding.btnRegisterNext.setOnClickListener {
            viewModel.requestRegisterList(
                binding.etEmailRegister.text.toString(),
                binding.etPw.text.toString(),
                binding.etCheckpw.text.toString()
            )
        }

        binding.ivRegisterBack.setOnClickListener {
            finish()
        }

        binding.ivRegisterX.setOnClickListener {
            finish()
        }

        binding.ivPwShowHidden.setOnClickListener {
            if (pwValue == LoginActivity.HIDDEN_PW) {
                pwValue = LoginActivity.SHOW_PW
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_show)
                binding.etPw.transformationMethod = null
                //커서 맨 뒤로
                binding.etPw.setSelection(binding.etPw.text.length)

            } else {
                pwValue = LoginActivity.HIDDEN_PW
                binding.ivPwShowHidden.setImageResource(R.drawable.ic_pw_hidden)
                binding.etPw.transformationMethod = PasswordTransformationMethod.getInstance()
                //커서 맨 뒤로
                binding.etPw.setSelection(binding.etPw.text.length)
            }
        }

        binding.ivCheckpwShowHidden.setOnClickListener {
            if (pwValue == LoginActivity.HIDDEN_PW) {
                pwValue = LoginActivity.SHOW_PW
                binding.ivCheckpwShowHidden.setImageResource(R.drawable.ic_pw_show)
                binding.etCheckpw.transformationMethod = null
                //커서 맨 뒤로
                binding.etCheckpw.setSelection(binding.etCheckpw.text.length)

            } else {
                pwValue = LoginActivity.HIDDEN_PW
                binding.ivCheckpwShowHidden.setImageResource(R.drawable.ic_pw_hidden)
                binding.etCheckpw.transformationMethod = PasswordTransformationMethod.getInstance()
                //커서 맨 뒤로
                binding.etCheckpw.setSelection(binding.etCheckpw.text.length)
            }
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

        binding.etCheckpw.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.ivCheckpwShowHidden.makeVisible()

            } else {
                if (binding.etCheckpw.text.isNullOrBlank()) {
                    binding.ivCheckpwShowHidden.makeInVisible()
                } else binding.ivCheckpwShowHidden.makeVisible()
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
        const val PASSWORD_FORMAT =
            "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,16}\$"
    }
}