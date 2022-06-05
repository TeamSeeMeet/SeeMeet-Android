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
    private var profile_position = DEFAULT
    private var nameId_position = DEFAULT
    private val binding: ActivityMyPageBinding by lazy {
        ActivityMyPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSetting()
        initClickListener()
    }

    fun initSetting() {
        if (!SeeMeetSharedPreference.getSocialLogin())
            binding.clChangepw.visibility = View.VISIBLE
        binding.etMypageName.setText(SeeMeetSharedPreference.getUserName())
        binding.etMypageId.setText(SeeMeetSharedPreference.getUserId())
    }

    fun initClickListener() {
        binding.backMypage.setOnClickListener {
            finish()
        }

        binding.btnProfileEditOrSave.setOnClickListener {
            when (profile_position) {
                DEFAULT -> {
                    binding.btnProfileEditOrSave.text = "프로필 사진 저장"
                    binding.btnSelectImage.visibility = View.VISIBLE
                    profile_position = ONEDITPROFILE
                }
                ONEDITPROFILE -> {
                    //프로필 사진 바꾸는 서버 연결
                    binding.btnProfileEditOrSave.text = "프로필 사진 편집"
                    binding.btnSelectImage.visibility = View.INVISIBLE
                    profile_position = DEFAULT
                }
            }
        }

        binding.btnSelectImage.setOnClickListener {
            //갤러리로 들어가기
        }

        binding.btnEditOrSave.setOnClickListener {
            when (nameId_position) {
                DEFAULT -> {
                    //수정하기 버튼을 누를 때
                    binding.btnMypageCancel.visibility = View.VISIBLE
                    binding.btnEditOrSave.text = "저장"
                    binding.etMypageName.setText(SeeMeetSharedPreference.getUserName())
                    binding.etMypageName.isEnabled = true
                    binding.mypageLine.visibility = View.VISIBLE
                    binding.etMypageId.setText(SeeMeetSharedPreference.getUserId())
                    binding.etMypageId.isEnabled = true
                    binding.mypageLine2.visibility = View.VISIBLE
                    nameId_position = ONEDITNAMEID
                }
                ONEDITNAMEID -> {
                    //저장하기 버튼을 누를 때
                    //이름, 아이디 바꾸는 서버 연결
                    binding.btnMypageCancel.visibility = View.INVISIBLE
                    binding.btnEditOrSave.text = "수정"
                    binding.etMypageName.setText(SeeMeetSharedPreference.getUserName())
                    binding.etMypageName.isEnabled = false
                    binding.mypageLine.visibility = View.INVISIBLE
                    binding.etMypageId.setText(SeeMeetSharedPreference.getUserId())
                    binding.etMypageId.isEnabled = false
                    binding.mypageLine2.visibility = View.INVISIBLE
                    nameId_position = DEFAULT
                }
            }
        }

        binding.btnMypageCancel.setOnClickListener {
            binding.btnMypageCancel.visibility = View.INVISIBLE
            binding.btnEditOrSave.text = "수정"
            binding.etMypageName.setText(SeeMeetSharedPreference.getUserName())
            binding.etMypageName.isEnabled = false
            binding.mypageLine.visibility = View.INVISIBLE
            binding.etMypageId.setText(SeeMeetSharedPreference.getUserId())
            binding.etMypageId.isEnabled = false
            binding.mypageLine2.visibility = View.INVISIBLE
            nameId_position = DEFAULT
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

        const val DEFAULT = 1
        const val ONEDITPROFILE = 2
        const val ONEDITNAMEID = 2
    }
}