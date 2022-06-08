package org.seemeet.seemeet.ui.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.databinding.ActivityChangePwBinding

class ChangePwActivity : AppCompatActivity() {
    private val binding: ActivityChangePwBinding by lazy {
        ActivityChangePwBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
    }

    fun initClickListener() {
        binding.btnBacKChangePw.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            //비밀번호 변경
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ChangePwActivity::class.java)
            context.startActivity(intent)
        }
    }
}