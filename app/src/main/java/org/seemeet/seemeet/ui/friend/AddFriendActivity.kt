package org.seemeet.seemeet.ui.friend

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.seemeet.seemeet.databinding.ActivityAddFriendBinding
import org.seemeet.seemeet.ui.viewmodel.AddFriendViewModel

class AddFriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFriendBinding
    private val viewModel: AddFriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
    }

    // 옵저버
    private fun setUserObserver() {
        //옵저버는 통신이 됐을 때만 데이터를 업데이트 해. 그래서 검색 결과가 없어도 이전 데이터가 불러와져.
        viewModel.userList.observe(this, Observer { userData ->
            binding.clFriendSearchList.visibility = View.VISIBLE
            binding.tvSearchFriendEmail.text = userData.data.email
            binding.tvSearchFriendName.text = userData.data.username
        })
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

    private fun initClickListener() {
        binding.ivFriendAddBack.setOnClickListener {
            finish()
        }

        // x버튼
        binding.etSearchFriendId.addTextChangedListener {
            if (binding.etSearchFriendId.text.isNullOrBlank()) { //공백일 때
                binding.ivFriendIdRemoveAll.visibility = View.GONE
            } else {
                binding.ivFriendIdRemoveAll.visibility = View.VISIBLE
                binding.ivFriendIdRemoveAll.setOnClickListener {
                    binding.etSearchFriendId.setText(null)
                }
            }
        }

        // 검색 enter 버튼
        binding.etSearchFriendId.setOnEditorActionListener { textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                val email = binding.etSearchFriendId.text
                viewModel.requestUserList(email)
                handled = true
                binding.tvSearchFriendNull.visibility = View.VISIBLE
                setUserObserver()
                binding.clFriendSearchList.visibility = View.GONE
            }
            handled
        }

        // 친구 추가 버튼
        binding.ivAddFriendList.setOnClickListener {
            binding.ivAddFriendList.isSelected = binding.ivAddFriendList.isSelected != true
        }

    }
}