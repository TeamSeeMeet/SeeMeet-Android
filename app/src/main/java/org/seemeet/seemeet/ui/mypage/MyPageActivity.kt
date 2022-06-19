package org.seemeet.seemeet.ui.mypage

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.api.RetrofitBuilder
import org.seemeet.seemeet.data.model.response.login.ExUser
import org.seemeet.seemeet.data.model.response.mypage.ResponseMyPageProfile
import org.seemeet.seemeet.databinding.ActivityMyPageBinding
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.MyPageViewModel
import org.seemeet.seemeet.util.CustomToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File


class MyPageActivity : AppCompatActivity() {
    private var profile_position = DEFAULT
    private var nameId_position = DEFAULT
    var currentImageUrl: String? = SeeMeetSharedPreference.getUserProfile()
    var prev_etId: String? = SeeMeetSharedPreference.getUserId()

    private val viewModel: MyPageViewModel by viewModels()

    private val binding: ActivityMyPageBinding by lazy {
        ActivityMyPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.myPageviewModel = viewModel
        binding.lifecycleOwner = this
        initSetting()
        statusObserver()
        initClickListener()
    }

    fun initSetting() {
        if (!SeeMeetSharedPreference.getSocialLogin())
            binding.clChangepw.visibility = View.VISIBLE

        if (!currentImageUrl.isNullOrEmpty()) {
            Glide.with(this).load(currentImageUrl!!.toUri()).circleCrop()
                .into(binding.ivMypageProfile)
        }

        viewModel.mypageName.postValue(SeeMeetSharedPreference.getUserName())
        viewModel.mypageId.postValue(SeeMeetSharedPreference.getUserId())
    }

    private fun statusObserver() {
        viewModel.fetchState.observe(this) {
            var message = ""
            when (it.second) {
                BaseViewModel.FetchState.BAD_INTERNET -> {
                    message = "소켓 오류 / 서버와 연결에 실패하였습니다."
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val error = (it.first as HttpException)
                    if (error.response()!!.errorBody()!!.string()
                            .split("\"")[7] == "이미 사용중인 닉네임입니다."
                    ) {
                        CustomToast.createToast(this, "이미 사용 중이에요")?.show()
                    }
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    message = "호스트를 확인할 수 없습니다. 네트워크 연결을 확인해주세요"
                }
                else -> {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }
            }
            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())
            if (message != "") {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.mypageStatus.observe(this) {
            if (it) {
                BtnSave()
            }
        }

        viewModel.mypageId.observe(this) {
            //그 직전 값이랑 입력값 비교해서 현재 커서 위치 알아내기
            var cursor_pos = it.length
            //입력한 경우
            if (it.length > 0 && prev_etId!!.length < it.length) {
                for (i in 0..prev_etId!!.length - 1) {
                    if (prev_etId!![i].lowercase() != it[i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                if (it[cursor_pos - 1] >= 'A' && it[cursor_pos - 1] <= 'Z') {
                    viewModel.mypageId.value = it.substring(
                        0,
                        cursor_pos - 1
                    ) + it[cursor_pos - 1].lowercase() + it.substring(cursor_pos)
                    viewModel.upperCase.value = true
                    viewModel.cursorPos.value = cursor_pos
                }
                prev_etId = it
            }
            //지운 경우(드래그 전체 선택 후 입력한 경우도 포함)
            if (it.length > 0 && prev_etId!!.length >= it.length) {
                for (i in 0..it.length - 1) {
                    if (it[i].toString() != prev_etId!![i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                if (it[cursor_pos - 1] >= 'A' && it[cursor_pos - 1] <= 'Z') {
                    viewModel.mypageId.value = it.substring(
                        0,
                        cursor_pos - 1
                    ) + it[cursor_pos - 1].lowercase() + it.substring(cursor_pos)
                    viewModel.upperCase.value = true
                    viewModel.cursorPos.value = cursor_pos
                }
                prev_etId = it
            }
        }

        viewModel.MyPageNameIdList.observe(this, Observer {
            setSharedPreference(it.data)
        })

        viewModel.warning.observe(this, Observer {
            if (it != "") {
                CustomToast.createToast(this, it)?.show()
            }
        })
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
                    changeProfile()
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
                    viewModel.check()
                    if (viewModel.status.value == true) {
                        viewModel.requestMyPageNameIdList(
                            binding.etMypageName.text.toString(),
                            binding.etMypageId.text.toString()
                        )
                    }
                }
            }
        }

        binding.btnMypageCancel.setOnClickListener {
            BtnCancel()
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
                    SeeMeetSharedPreference.clearStorage()
                    val intent = Intent(this@MyPageActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //기존에 쌓여있던 액티비티를 삭제
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
                    viewModel.requestMyPageWithdrawal()
                    SeeMeetSharedPreference.clearStorage()
                    val intent = Intent(this@MyPageActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //기존에 쌓여있던 액티비티를 삭제
                    this@MyPageActivity.startActivity(intent)
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }
    }

    //프로필 사진 변경하는 함수
    private fun changeProfile() {
        // 사진이 똑같은 경우 서버 통신 안함
        if (SeeMeetSharedPreference.getUserProfile() == currentImageUrl) {
            binding.btnProfileEditOrSave.text = "프로필 사진 편집"
            binding.btnSelectImage.visibility = View.INVISIBLE
            profile_position = DEFAULT
        }
        // 사진이 변경된 경우 서버 통신
        else {
            val url = currentImageUrl?.toUri()
            //절대 경로 받아오기
            val file = File(getRealPathFromURI(url!!))
            val requestFile =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            RetrofitBuilder.mypageService.postProfile(
                SeeMeetSharedPreference.getToken(),
                body
            )    // body 가 Multipart.Part
                .enqueue(object : Callback<ResponseMyPageProfile> {
                    override fun onFailure(
                        call: Call<ResponseMyPageProfile>,
                        t: Throwable
                    ) {
                        Log.e("error : ", t.message ?: return)
                    }

                    override fun onResponse(
                        call: Call<ResponseMyPageProfile>,
                        response: Response<ResponseMyPageProfile>
                    ) {
                        SeeMeetSharedPreference.setUserProfile(currentImageUrl)
                        binding.btnProfileEditOrSave.text = "프로필 사진 편집"
                        binding.btnSelectImage.visibility = View.INVISIBLE
                        profile_position = DEFAULT
                    }
                })
        }
    }

    //content uri를 File path로 바꿔주는 함수
    fun getRealPathFromURI(contentUri: Uri): String? {
        if (contentUri.path!!.startsWith("/storage")) {
            return contentUri.path!!
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":")[1]
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = MediaStore.Files.FileColumns._ID + " = " + id
        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            columns,
            selection,
            null,
            null
        )
        try {
            val columnIndex = cursor!!.getColumnIndex(columns[0])
            if (cursor!!.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor!!.close()
        }
        return null
    }

    //갤러리 들어가는 함수
    private val OPEN_GALLERY = 1
    private fun openGallery() {
        /* 권한 받기 */
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 200)
        } else {
            //허용했을 경우
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*")
            startActivityForResult(intent, OPEN_GALLERY)
        }
    }

    //권한 관련 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            200 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setType("image/*")
                    startActivityForResult(intent, OPEN_GALLERY)
                } else {
                    CustomToast.createToast(this@MyPageActivity, "스토리지에 접근 권한을 허가해주세요")?.show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                // 갤러리에서 이미지 가져온 경우
                currentImageUrl = data?.data.toString()
                Log.d("갤러리에서 가져온 사진 url", currentImageUrl.toString())
                try {
                    Glide.with(this).load(data?.data).circleCrop()
                        .into(binding.ivMypageProfile)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // sharedPreference setting
    private fun setSharedPreference(list: ExUser) {
        SeeMeetSharedPreference.setUserId(list.nickname ?: return)
        SeeMeetSharedPreference.setUserName(list.username)
    }

    private fun BtnEdit() {
        //수정 버튼을 누를 경우
        binding.btnMypageCancel.visibility = View.VISIBLE
        binding.btnEditOrSave.text = "저장"
        binding.etMypageName.isEnabled = true
        binding.etMypageName.setText(SeeMeetSharedPreference.getUserName())
        binding.mypageLine.visibility = View.VISIBLE
        binding.etMypageId.isEnabled = true
        binding.etMypageId.setText(SeeMeetSharedPreference.getUserId())
        binding.mypageLine2.visibility = View.VISIBLE
        nameId_position = ONEDITNAMEID
    }

    private fun BtnSave() {
        //저장 버튼을 누를 경우
        binding.btnMypageCancel.visibility = View.INVISIBLE
        binding.btnEditOrSave.text = "수정"
        binding.etMypageName.setText(binding.etMypageName.text.toString())
        binding.etMypageName.isEnabled = false
        binding.mypageLine.visibility = View.INVISIBLE
        binding.etMypageId.setText(binding.etMypageId.text.toString())
        binding.etMypageId.isEnabled = false
        binding.mypageLine2.visibility = View.INVISIBLE
        nameId_position = DEFAULT
    }

    private fun BtnCancel() {
        // 취소 버튼을 누를 경우
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
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