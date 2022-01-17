package org.seemeet.seemeet.ui.friend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import org.seemeet.seemeet.databinding.ActivityFriendBinding
import org.seemeet.seemeet.ui.friend.adapter.FriendListAdapter
import org.seemeet.seemeet.ui.viewmodel.FriendViewModel

class FriendActivity : AppCompatActivity() {
    private val friendAdapter = FriendListAdapter()
    private lateinit var binding: ActivityFriendBinding
    private val viewModel: FriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setFriendList()

        setFriendAdapter()
        setFriendObserver()
        initClickListener()
    }

    // 어댑터
    private fun setFriendAdapter() {
        binding.rvFriend.adapter = friendAdapter
    }

    // 옵저버
    private fun setFriendObserver() {
        viewModel.friendList.observe(this, Observer { friendList ->
            with(binding.rvFriend.adapter as FriendListAdapter) {
                setFriendList(friendList)

                if (friendList.isEmpty()) {
                    binding.clFriendNull.visibility = View.VISIBLE
                } else {
                    binding.clFriendNull.visibility = View.GONE
                }
            }
        })

    }

    private fun initClickListener() {
        binding.ivAddFriend.setOnClickListener {
            val nextIntent = Intent(this, AddFriendActivity::class.java)
            startActivity(nextIntent)
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

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FriendActivity::class.java)
            context.startActivity(intent)
        }
    }

}