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
import org.seemeet.seemeet.ui.friend.adapter.FriendListAdapter
import org.seemeet.seemeet.ui.receive.DialogHomeNoLoginFragment
import org.seemeet.seemeet.ui.registration.LoginActivity
import org.seemeet.seemeet.ui.viewmodel.FriendViewModel

class FriendActivity : AppCompatActivity() {
    private val friendAdapter = FriendListAdapter()
    private lateinit var binding: ActivityFriendBinding
    private val viewModel: FriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(SeeMeetSharedPreference.getLogin()) {
            viewModel.requestFriendList()
        } else {
            setNullFriendList()
        }
        setFriendAdapter()
        setFriendObserver()
        initClickListener()
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

    // 어댑터
    private fun setFriendAdapter() {
        binding.rvFriend.adapter = friendAdapter
    }


    // 옵저버
    private fun setFriendObserver() {
        viewModel.friendList.observe(this, Observer { friendList ->
            with(binding.rvFriend.adapter as FriendListAdapter) {
                setFriendList(friendList.data)

                if (friendList.data.isEmpty()) {
                    binding.clFriendNull.visibility = View.VISIBLE
                } else {
                    binding.clFriendNull.visibility = View.GONE
                }
            }
        })

    }

    private fun setNullFriendList() {
        binding.clFriendNull.visibility = View.VISIBLE
    }

    private fun initClickListener() {
        binding.ivAddFriend.setOnClickListener {

            if(SeeMeetSharedPreference.getLogin()){
                val nextIntent = Intent(this, AddFriendActivity::class.java)
                startActivity(nextIntent)
            }
            else
                setNoLoginDailog()
        }

        binding.ivFriendBack.setOnClickListener {
            finish()
        }

        binding.etSearchFriend.addTextChangedListener {
            if (binding.etSearchFriend.text.isNullOrBlank()) { //공백일 때
                binding.ivFriendRemoveAll.visibility = View.GONE
            } else {
                binding.ivFriendRemoveAll.visibility = View.VISIBLE
                binding.ivFriendRemoveAll.setOnClickListener {
                    binding.etSearchFriend.setText(null)
                }
            }
            
            friendAdapter.setSearchWord(binding.etSearchFriend.text.toString())
        }
    }

    private fun setNoLoginDailog(){

        val dialogView = DialogHomeNoLoginFragment()

        dialogView.setButtonClickListener( object :  DialogHomeNoLoginFragment.OnButtonClickListener {
            override fun onCancelClicked() {
            }
            override fun onLoginClicked() {
                val nextIntent = Intent(this@FriendActivity, LoginActivity::class.java)
                startActivity(nextIntent)
            }
        })
        dialogView.show(supportFragmentManager, "send wish checkbox time")

    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FriendActivity::class.java)
            context.startActivity(intent)
        }
    }

}