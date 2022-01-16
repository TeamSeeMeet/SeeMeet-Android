package org.seemeet.seemeet.ui.friend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityFriendBinding
import org.seemeet.seemeet.ui.friend.adapter.FriendListAdapter
import org.seemeet.seemeet.ui.viewmodel.FriendViewModel

class FriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendBinding
    private val viewModel: FriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend)
        binding.viewModel = viewModel
        viewModel.setFriendList()

        setFriendAdapter()
        setFriendObserver()
        initClickListener()
    }

    private fun setFriendAdapter() {
        val friendAdapter = FriendListAdapter()

        binding.rvFriend.adapter = friendAdapter
    }

    // 옵저버
    private fun setFriendObserver() {
        viewModel.friendList.observe(this, Observer {
            friendList -> with(binding.rvFriend.adapter as FriendListAdapter){
                setFriendList(friendList)
            }
        })

    }

    private fun initClickListener(){
        binding.ivAddFriend.setOnClickListener {
            val nextIntent = Intent(this, AddFriendActivity::class.java)
            startActivity(nextIntent)
        }

        binding.ivFriendBack.setOnClickListener {
            finish()
        }

        binding.etSearchFriend.setOnKeyListener { _, keyCode, event ->
            if ((event.action== KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearchFriend.windowToken, 0)
            }
            true
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FriendActivity::class.java)
            context.startActivity(intent)
        }
    }

}