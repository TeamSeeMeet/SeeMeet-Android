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
import org.seemeet.seemeet.data.model.response.friend.UserListData
import org.seemeet.seemeet.databinding.ActivityAddFriendBinding
import org.seemeet.seemeet.ui.friend.adapter.UserListAdapter
import org.seemeet.seemeet.ui.viewmodel.AddFriendViewModel

class AddFriendActivity : AppCompatActivity() {
    private var userAdapter = UserListAdapter()
    private lateinit var binding: ActivityAddFriendBinding
    private val viewModel: AddFriendViewModel by viewModels()
    private lateinit var userlist: List<UserListData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
        setUserAdapter()
   }

    private fun initClickListener() {
        // x버튼
        binding.ivFriendAddBack.setOnClickListener {
            finish()
        }

        // 입력창 리스너 (x버튼)
        binding.etSearchUser.addTextChangedListener {
            if (binding.etSearchUser.text.isNullOrBlank()) { //공백일 때
                setVisibility(binding.ivRemoveAll, View.GONE)
            } else {
                setVisibility(binding.ivRemoveAll, View.VISIBLE)
                binding.ivRemoveAll.setOnClickListener {
                    binding.etSearchUser.text = null
                }
            }
        }

        // 검색 enter 버튼
        binding.etSearchUser.setOnEditorActionListener { _, action, _ ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                val nickname = binding.etSearchUser.text
                viewModel.requestUserList(nickname)
                handled = true
                setVisibility(binding.tvSearchUserNull, View.VISIBLE)
                setUserObserver()
                setVisibility(binding.rvUserSearch, View.GONE)
            }
            handled
        }
    }

    private fun setUserAdapter(){
        binding.rvUserSearch.adapter = userAdapter
    }

    private fun setUserObserver(){
        viewModel.userList.observe(this, Observer { userData ->
            with(binding.rvUserSearch.adapter as UserListAdapter) {
                setVisibility(binding.rvUserSearch, View.VISIBLE)
                setUserList(userData.data)
                userlist = mutableListOf(userData.data)
                initItemClickListener()
            }
        })
    }

    private fun initItemClickListener(){
        userAdapter.setOnItemClickListener{ _, pos ->
            //for (changePos in userList.indices) {
                //if (listOf(viewModel.userList.value?.data)[changePos]?.nickname == userList[pos].nickname) {
                    val nickname = userlist[pos].nickname
                    viewModel.requestAddFriend(nickname)
                //}
            //}
        }
    }

    private fun setVisibility(view: View, visibility: Int){
        view.visibility = visibility
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
}