package org.seemeet.seemeet.ui.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.ui.MainActivity
import org.seemeet.seemeet.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()
    }

    fun initClickListener(){
        binding.btnRegister.setOnClickListener {
            MainActivity.start(this)
        }
    }
}
