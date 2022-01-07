package org.seemeet.seemeet.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.MainActivity
import org.seemeet.seemeet.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        LoginBtn()
        RegisterBtn()
    }

    fun LoginBtn() {
        binding.btnLogin.setOnClickListener {
            MainActivity.start(this)
        }
    }

    fun RegisterBtn() {
        binding.tvRegister.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }
    }
}