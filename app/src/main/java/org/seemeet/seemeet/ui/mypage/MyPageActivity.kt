package org.seemeet.seemeet.ui.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.ActivityMyPageBinding
import org.seemeet.seemeet.ui.registration.LoginMainActivity

class MyPageActivity : AppCompatActivity() {
    private var profile_position = DEFAULT
    private var nameId_position = DEFAULT
    var currentImageUrl: String? = SeeMeetSharedPreference.getUserProfile()

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

        if (!currentImageUrl.isNullOrEmpty()) {
            Glide.with(this).load(currentImageUrl!!.toUri()).circleCrop()
                .into(binding.ivMypageProfile)
        }

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
                    SeeMeetSharedPreference.setUserProfile(currentImageUrl)
                    binding.btnProfileEditOrSave.text = "프로필 사진 편집"
                    binding.btnSelectImage.visibility = View.INVISIBLE
                    profile_position = DEFAULT
                }
            }
        }

        binding.btnSelectImage.setOnClickListener {
            //갤러리로 들어가기
            openGallery()
        }

        binding.btnEditOrSave.setOnClickListener {
            when (nameId_position) {
                DEFAULT -> {
                    //수정하기 버튼을 누를 때
                    BtnEdit()
                }
                ONEDITNAMEID -> {
                    //저장하기 버튼을 누를 때
                    //이름, 아이디 바꾸는 서버 연결
                    BtnSave()
                }
            }
        }

        binding.btnMypageCancel.setOnClickListener {
            BtnSave()
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

    private val OPEN_GALLERY = 1
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                var ImageUrl: Uri? = data?.data
                currentImageUrl = ImageUrl.toString()
                try {
                    Glide.with(this).load(ImageUrl).circleCrop()
                        .into(binding.ivMypageProfile)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
        }
    }

    private fun BtnEdit() {
        binding.btnMypageCancel.visibility = View.VISIBLE
        binding.btnEditOrSave.text = "저장"
        binding.etMypageName.isEnabled = true
        binding.mypageLine.visibility = View.VISIBLE
        binding.etMypageId.isEnabled = true
        binding.mypageLine2.visibility = View.VISIBLE
        nameId_position = ONEDITNAMEID
    }

    private fun BtnSave() {
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