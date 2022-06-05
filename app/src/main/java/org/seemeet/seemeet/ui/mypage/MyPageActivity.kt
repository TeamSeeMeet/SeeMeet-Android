package org.seemeet.seemeet.ui.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.ActivityMyPageBinding
import org.seemeet.seemeet.ui.main.home.ChangePwActivity
import org.seemeet.seemeet.ui.registration.LoginMainActivity

class MyPageActivity : AppCompatActivity() {
    private val binding: ActivityMyPageBinding by lazy {
        ActivityMyPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSetting()
        initClickListener()
    }

    fun initSetting(){
        if(!SeeMeetSharedPreference.getSocialLogin())
            binding.clChangepw.visibility = View.VISIBLE
    }

    fun initClickListener() {
        binding.backMypage.setOnClickListener {
            finish()
        }

        binding.clChangepw.setOnClickListener {
            ChangePwActivity.start(this)
        }

        binding.btnLogout.setOnClickListener {
            val dialogView = DialogLogoutFragment()
            val bundle = Bundle()

            dialogView.arguments = bundle

            dialogView.setButtonClickListener(object : DialogLogoutFragment.OnButtonClickListener {
                override fun onLogoutNoClicked() {
                }

                override fun onLogoutYesClicked() {
                    val intent = Intent(this@MyPageActivity, LoginMainActivity::class.java)
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //기존에 쌓여있던 액티비티를 삭제
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    SeeMeetSharedPreference.clearStorage()
                    this@MyPageActivity.startActivity(intent)
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }

        binding.btnResign.setOnClickListener {
            //탈퇴하기
            val dialogView = DialogResignFragment()
            val bundle = Bundle()

            dialogView.arguments = bundle

            dialogView.setButtonClickListener(object : DialogResignFragment.OnButtonClickListener {
                override fun onResignNoClicked() {
                }

                override fun onResignYesClicked() {
                    //탈퇴 서버 연결
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MyPageActivity::class.java)
            context.startActivity(intent)
        }
    }
}