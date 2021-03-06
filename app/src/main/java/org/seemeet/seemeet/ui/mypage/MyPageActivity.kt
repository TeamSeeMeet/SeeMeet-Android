package org.seemeet.seemeet.ui.mypage

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.model.response.login.ExUser
import org.seemeet.seemeet.databinding.ActivityMyPageBinding
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.MyPageViewModel
import org.seemeet.seemeet.util.CustomToast
import retrofit2.HttpException
import java.io.File
import java.util.regex.Pattern


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
        if (intent.getBooleanExtra("NoNameId", false))
            BtnEdit()
        initSetting()
        statusObserver()
        initClickListener()
    }

    fun initSetting() {
        if (!SeeMeetSharedPreference.getSocialLogin())
            binding.clChangepw.visibility = View.VISIBLE

        if (currentImageUrl == "null" || currentImageUrl.isNullOrEmpty()) {
            Glide.with(this).load(R.drawable.ic_img_profile).circleCrop()
                .into(binding.ivMypageProfile)
        } else {
            Glide.with(this).load(currentImageUrl!!.toUri())
                .circleCrop()
                .into(binding.ivMypageProfile)
        }

        viewModel.mypageName.postValue(SeeMeetSharedPreference.getUserName())
        viewModel.mypageId.postValue(SeeMeetSharedPreference.getUserId())
    }

    fun inputCase(it: String, cursor_pos: Int) {
        // ???????????? ???????????? ???????????? ??????
        if (it[cursor_pos - 1] >= 'A' && it[cursor_pos - 1] <= 'Z') {
            viewModel.mypageId.value = it.substring(
                0,
                cursor_pos - 1
            ) + it[cursor_pos - 1].lowercase() + it.substring(cursor_pos)
            viewModel.id_upperCase.value = true
            viewModel.id_cursorPos.value = cursor_pos
        }
        prev_etId = it

    }

    private fun statusObserver() {
        viewModel.fetchState.observe(this) {
            var message = ""
            when (it.second) {
                BaseViewModel.FetchState.BAD_INTERNET -> {
                    message = "?????? ?????? / ????????? ????????? ?????????????????????."
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val error = (it.first as HttpException)
                    if (error.response()!!.errorBody()!!.string()
                            .split("\"")[7] == "?????? ???????????? ??????????????????."
                    ) {
                        CustomToast.createToast(this, "?????? ?????? ????????????")?.show()
                    }
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    message = "???????????? ????????? ??? ????????????. ???????????? ????????? ??????????????????"
                }
                else -> {
                    message = "????????? ?????????????????????.\n ${it.first.message}"
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

        viewModel.profileStatus.observe(this) {
            if (it == 0) {
                Toast.makeText(this, "?????? ?????? / ????????? ????????? ?????????????????????.", Toast.LENGTH_LONG).show()
            } else if (it == 1) {
                CustomToast.createToast(this, "????????? ????????? ?????????????????????")!!.show()
                SeeMeetSharedPreference.setUserProfile(currentImageUrl)
                binding.btnProfileEditOrSave.text = "????????? ?????? ??????"
                binding.btnSelectImage.visibility = View.INVISIBLE
                binding.camera.visibility = View.INVISIBLE
                profile_position = DEFAULT
            }
        }

        viewModel.mypageId.observe(this) {
            var cursor_pos = it.length
            if (it.length > 0 && prev_etId!!.length < it.length) {
                for (i in 0..prev_etId!!.length - 1) {
                    if (prev_etId!![i].lowercase() != it[i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                inputCase(it, cursor_pos)
            }
            if (it.length > 0 && prev_etId!!.length >= it.length) {
                for (i in 0..it.length - 1) {
                    if (it[i].toString() != prev_etId!![i].lowercase()) {
                        cursor_pos = i + 1
                        break
                    }
                }
                inputCase(it, cursor_pos)
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
        binding.etMypageName.setFilters(arrayOf(InputFilter { src, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (src.matches(Regex("^[???-???|???-???|a-z|A-Z]*$"))) {
                    return@InputFilter src
                }
                return@InputFilter ""
            }
            null
        }, LengthFilter(5)))

        binding.backMypage.setOnClickListener {
            finish()
        }

        binding.btnProfileEditOrSave.setOnClickListener {
            when (profile_position) {
                DEFAULT -> {
                    binding.btnProfileEditOrSave.text = "????????? ?????? ??????"
                    binding.btnSelectImage.visibility = View.VISIBLE
                    binding.camera.visibility = View.VISIBLE
                    profile_position = ONEDITPROFILE
                }
                ONEDITPROFILE -> {
                    //????????? ?????? ????????? ?????? ??????
                    changeProfile()
                }
            }
        }

        binding.btnSelectImage.setOnClickListener {
            //???????????? ????????????
            openGallery()
        }

        binding.btnEditOrSave.setOnClickListener {
            when (nameId_position) {
                DEFAULT -> {
                    //???????????? ????????? ?????? ???
                    BtnEdit()
                }
                ONEDITNAMEID -> {
                    //???????????? ????????? ?????? ???
                    //??????, ????????? ????????? ?????? ??????
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
                    //fcm ?????? null ??????
                    viewModel.requestPushTokenNull()

                    SeeMeetSharedPreference.clearStorage()
                    val intent = Intent(this@MyPageActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //????????? ???????????? ??????????????? ??????
                    this@MyPageActivity.startActivity(intent)
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }

        binding.btnResign.setOnClickListener {
            //????????????
            val dialogView = DialogResignFragment()
            val bundle = Bundle()

            dialogView.arguments = bundle

            dialogView.setButtonClickListener(object : DialogResignFragment.OnButtonClickListener {
                override fun onResignNoClicked() {
                }

                override fun onResignYesClicked() {
                    //?????? ?????? ??????
                    viewModel.requestMyPageWithdrawal()
                    //fcm ?????? null ??????
                    viewModel.requestPushTokenNull()
                    SeeMeetSharedPreference.clearStorage()
                    val intent = Intent(this@MyPageActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //????????? ???????????? ??????????????? ??????
                    this@MyPageActivity.startActivity(intent)
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }
    }

    //????????? ?????? ???????????? ??????
    private fun changeProfile() {
        // ????????? ????????? ?????? ?????? ?????? ??????
        if (SeeMeetSharedPreference.getUserProfile() == currentImageUrl) {
            binding.btnProfileEditOrSave.text = "????????? ?????? ??????"
            binding.btnSelectImage.visibility = View.INVISIBLE
            binding.camera.visibility = View.INVISIBLE
            profile_position = DEFAULT
        }
        // ????????? ????????? ?????? ?????? ??????
        else {
            val url = currentImageUrl?.toUri()

            //?????? ?????? ????????????
            val file = File(getPath(this, url!!))
            val requestFile =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            viewModel.requestMypageProfile(body = body)
            CustomToast.createToast(this, "????????? ?????? ????????????. ????????? ??????????????????!")!!.show()
        }
    }

    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    //????????? ???????????? ??????
    private val OPEN_GALLERY = 1
    private fun openGallery() {
        /* ?????? ?????? */
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*")
            startActivityForResult(intent, OPEN_GALLERY)
        } else {
            val permissions: Array<String> = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            ActivityCompat.requestPermissions(this, permissions, 0)
        }
    }

    //?????? ?????? ??????
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty()) {
                    var isAllGranted = true
                    // ????????? ?????? ??????/?????? ?????? ????????? ??????
                    for (grant in grantResults) {
                        if (grant != PackageManager.PERMISSION_GRANTED) {
                            isAllGranted = false
                            break;
                        }
                    }
                    // ????????? ????????? ?????? ????????????.
                    if (isAllGranted) {
                        // ?????? step?????? ~
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.setType("image/*")
                        startActivityForResult(intent, OPEN_GALLERY)
                    }
                    // ???????????? ?????? ????????? ??????. ????????????/???????????? ????????? ????????? ?????? ????????? ???????????? ???.
                    else {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            // ?????? ?????? ?????? ??????????????? ?????? ?????? ?????????.
                            CustomToast.createToast(this@MyPageActivity, "??????????????? ?????? ????????? ??????????????????")
                                ?.show()
                        } else {
                            // ?????? ?????? ???????????????.
                            CustomToast.createToast(this@MyPageActivity, "??????????????? ?????? ????????? ??????????????????")
                                ?.show()
                        }
                    }
                }
            }
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                // ??????????????? ????????? ????????? ??????
                currentImageUrl = data?.data.toString()
                Log.d("??????????????? ????????? ?????? url", currentImageUrl.toString())
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
        //?????? ????????? ?????? ??????
        binding.btnMypageCancel.visibility = View.VISIBLE
        binding.btnEditOrSave.text = "??????"
        binding.etMypageName.isEnabled = true
        binding.etMypageName.setText(SeeMeetSharedPreference.getUserName())
        binding.mypageLine.visibility = View.VISIBLE
        binding.etMypageId.isEnabled = true
        binding.etMypageId.setText(SeeMeetSharedPreference.getUserId())
        binding.mypageLine2.visibility = View.VISIBLE
        nameId_position = ONEDITNAMEID
    }

    private fun BtnSave() {
        //?????? ????????? ?????? ??????
        CustomToast.createToast(this, "??????, ???????????? ?????????????????????")!!.show()
        binding.btnMypageCancel.visibility = View.INVISIBLE
        binding.btnEditOrSave.text = "??????"
        binding.etMypageName.setText(binding.etMypageName.text.toString())
        binding.etMypageName.isEnabled = false
        binding.mypageLine.visibility = View.INVISIBLE
        binding.etMypageId.setText(binding.etMypageId.text.toString())
        binding.etMypageId.isEnabled = false
        binding.mypageLine2.visibility = View.INVISIBLE
        nameId_position = DEFAULT
    }

    private fun BtnCancel() {
        // ?????? ????????? ?????? ??????
        binding.btnMypageCancel.visibility = View.INVISIBLE
        binding.btnEditOrSave.text = "??????"
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

        fun isNameFormat(password: String): Boolean {
            return Pattern.matches("^[???-???|???-???|a-z|A-Z]*$", password)
        }

        const val DEFAULT = 1
        const val ONEDITPROFILE = 2
        const val ONEDITNAMEID = 2
    }
}