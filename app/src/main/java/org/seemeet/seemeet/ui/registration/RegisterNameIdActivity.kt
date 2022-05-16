package org.seemeet.seemeet.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.request.login.RequestKakaoLogin
import org.seemeet.seemeet.data.model.request.register.RequestRegisterNameId
import org.seemeet.seemeet.data.model.response.login.ExUser
import org.seemeet.seemeet.databinding.ActivityRegisterNameIdActivityBinding
import org.seemeet.seemeet.ui.main.MainActivity
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
            if (binding.etId.text.length < 7) {
                binding.tvWarningId.text = resources.getString(R.string.register_lengthId)
                binding.tvWarningId.makeVisible()
            } else { //7자 이상인 경우
                if (isNumberFormat(binding.etId.text.toString())) {//숫자로만 이루어진 경우
                    binding.tvWarningId.text = resources.getString(R.string.register_numberId)
                    binding.tvWarningId.makeVisible()
                } else {
                    if (!isIdFormat(binding.etId.text.toString())) {
                        binding.tvWarningId.text =  resources.getString(R.string.register_formatId)
                        binding.tvWarningId.makeVisible()
                    } else binding.tvWarningId.makeInVisible()
                }
            }
            if (binding.etId.text.isNullOrBlank())
                binding.tvWarningId.makeInVisible()
            if (isNullOrBlank()) {
                binding.btnStart.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else {
                binding.btnStart.activeBtn()
            }
        }

        binding.btnStart.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val body = RetrofitBuilder.registerService.putRegisterNameId(
                        SeeMeetSharedPreference.getToken(),
                        RequestRegisterNameId(
                            binding.etName.text.toString(),
                            binding.etId.text.toString()
                        )
                    )
                    setSharedPreference(body.data)
                    MainActivity.start(this@RegisterNameIdActivity)
                } catch (e: Exception) {
                    Log.e("network error", e.toString())
                }
            }
        }
    }

    // sharedPreference setting
    private fun setSharedPreference(list : ExUser) {
        SeeMeetSharedPreference.setUserId(list.id)
        SeeMeetSharedPreference.setLogin(true)
        SeeMeetSharedPreference.setUserName(list.username)
    }

    private fun isNumberFormat(password: String): Boolean {
        return Pattern.matches("^[0-9]*\$", password)
    }

    private fun isIdFormat(password: String): Boolean {
        return Pattern.matches("^[a-z0-9._]*\$", password)
    }

    private fun isNullOrBlank(): Boolean { //하나라도 성립하면 true 반환
        return binding.tvWarningId.isVisible ||
                binding.etName.text.isNullOrBlank() ||
                binding.etId.text.isNullOrBlank()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterNameIdActivity::class.java)
            context.startActivity(intent)
        }
    }
}
