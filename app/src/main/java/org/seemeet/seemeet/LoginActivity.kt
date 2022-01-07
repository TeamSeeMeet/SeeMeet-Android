package org.seemeet.seemeet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.seemeet.seemeet.databinding.ActivityLoginBinding
import org.seemeet.seemeet.databinding.ActivityMainBinding

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