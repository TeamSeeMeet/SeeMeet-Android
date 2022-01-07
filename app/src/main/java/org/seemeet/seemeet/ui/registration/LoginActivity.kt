package org.seemeet.seemeet.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.seemeet.seemeet.MainActivity
import org.seemeet.seemeet.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            MainActivity.start(this)
        }
    }
}