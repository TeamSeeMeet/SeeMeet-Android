package org.seemeet.seemeet.ui.friend

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.ActivityFriendBinding
import org.seemeet.seemeet.ui.apply.ApplyActivity
import org.seemeet.seemeet.ui.friend.adapter.FriendListAdapter
import org.seemeet.seemeet.ui.receive.DialogHomeNoLoginFragment
import org.seemeet.seemeet.ui.registration.LoginActivity
import org.seemeet.seemeet.ui.viewmodel.FriendViewModel

class FriendActivity : AppCompatActivity() {
    private var friendAdapter = FriendListAdapter()
    private lateinit var binding: ActivityFriendBinding
    private val viewModel: FriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLogin()
        setFriendAdapter()
        setFriendObserver()
        initClickListener()
    }

    private fun setLogin() {
        if (SeeMeetSharedPreference.getLogin()) {
            viewModel.requestFriendList()
        } else {
            setVisibility(binding.clFriendNull, View.VISIBLE)
        }
    }

    private fun setFriendAdapter() {
        friendAdapter.setOnItemClickListener { _, pos ->
            initIntent(pos)
        }
        binding.rvFriend.adapter = friendAdapter
    }

    private fun setFriendObserver() {
        viewModel.friendList.observe(this, Observer { friendList ->
            with(binding.rvFriend.adapter as FriendListAdapter) {
                setFriendList(friendList.data)

                if (friendList.data.isEmpty()) {
                    setVisibility(binding.clFriendNull, View.VISIBLE)
                } else {
                    setVisibility(binding.clFriendNull, View.GONE)
                }
            }
        })
    }

    private fun initClickListener() {
        // 친구 추가 버튼
        binding.ivAddFriend.setOnClickListener {
            if (SeeMeetSharedPreference.getLogin()) {
                val nextIntent = Intent(this, AddFriendActivity::class.java)
                startActivity(nextIntent)
            } else {
                setNoLoginDialog()
            }
        }

        // 뒤로가기 버튼
        binding.ivFriendBack.setOnClickListener {
            finish()
        }

        // 입력창 리스너 (x버튼, 필터링)
        binding.etSearchFriend.addTextChangedListener {
            if (binding.etSearchFriend.text.isNullOrBlank()) { //공백일 때
                setVisibility(binding.ivFriendRemoveAll, View.GONE)
            } else {
                setVisibility(binding.ivFriendRemoveAll, View.VISIBLE)
                binding.ivFriendRemoveAll.setOnClickListener {
                    binding.etSearchFriend.text = null
                }
            }
            friendAdapter.setSearchWord(binding.etSearchFriend.text.toString())
        }
    }

    private fun initIntent(pos: Int) {
        val intent = Intent(this, ApplyActivity::class.java)
        intent.putExtra(
            "username",
            viewModel.friendList.value?.let { friendList -> friendList.data[pos].username })
        intent.putExtra(
            "userposition",pos)
        intent.putExtra("useremail", viewModel.friendList.value?.let { friendList -> friendList.data[pos].email })
        intent.putExtra("userid",viewModel.friendList.value?.let { friendList -> friendList.data[pos].id })
        startActivity(intent)
        finish()
    }

    private fun setNoLoginDialog() {
        val dialogView = DialogHomeNoLoginFragment()
        dialogView.setButtonClickListener(object : DialogHomeNoLoginFragment.OnButtonClickListener {
            override fun onCancelClicked() {
            }

            override fun onLoginClicked() {
                LoginActivity.start(this@FriendActivity)
            }
        })
        dialogView.show(supportFragmentManager, "add friend with login")
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

    override fun onResume() {
        super.onResume()
        if (SeeMeetSharedPreference.getLogin()) {
            viewModel.requestFriendList()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FriendActivity::class.java)
            context.startActivity(intent)
        }
    }
}