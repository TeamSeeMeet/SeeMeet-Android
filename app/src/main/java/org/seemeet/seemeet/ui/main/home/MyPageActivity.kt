package org.seemeet.seemeet.ui.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.ActivityMyPageBinding
import org.seemeet.seemeet.ui.registration.LoginMainActivity

class MyPageActivity : AppCompatActivity() {
    //private val viewModel: LoginViewModel by viewModels()
    private val binding: ActivityMyPageBinding by lazy {
        ActivityMyPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragment = FirstMyPageFragment()
        supportFragmentManager.beginTransaction().addToBackStack(null)
        supportFragmentManager.beginTransaction().add(R.id.container_mypage, fragment).commit()
        supportFragmentManager.beginTransaction().addToBackStack(null)

        binding.backMypage.setOnClickListener {
            finish()
        }

        binding.btnChangePw.setOnClickListener {
            //비번 바꾸는 창으로 넘어가기
            ChangePwActivity.start(this)
        }

        binding.btnLogout.setOnClickListener {
            SeeMeetSharedPreference.clearStorage()
            LoginMainActivity.start(this)
        }

        binding.btnResign.setOnClickListener {
            //탈퇴하기
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MyPageActivity::class.java)
            context.startActivity(intent)
        }
    }
}