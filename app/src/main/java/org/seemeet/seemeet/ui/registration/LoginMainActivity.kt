package org.seemeet.seemeet.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.databinding.ActivityLoginMainBinding


class LoginMainActivity : AppCompatActivity() {
    private val binding: ActivityLoginMainBinding by lazy {
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
    }

    fun initClickListener() {
        binding.kakaoLogo.setOnClickListener {

        }

        binding.tvEmailRegister.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }

        binding.tvEmailLogin.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginMainActivity::class.java)
            context.startActivity(intent)
        }
    }
}