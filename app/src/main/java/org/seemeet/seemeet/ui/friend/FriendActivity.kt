package org.seemeet.seemeet.ui.friend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.data.local.FriendData
import org.seemeet.seemeet.databinding.ActivityFriendBinding
import org.seemeet.seemeet.ui.friend.adapter.FriendAdapter
import org.seemeet.seemeet.ui.friend.adapter.FriendRequestAdapter

class FriendActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFriendBinding
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var friendRequestAdapter: FriendRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFriendAdapter()
        initFriendFavoriteAdapter()
        initFriendRequestAdapter()
        initClickListener()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FriendActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun initFriendAdapter(){
        friendAdapter = FriendAdapter()
        binding.rvFriend.adapter = friendAdapter
        friendAdapter.friendList.addAll(
            listOf(
                FriendData("오수린"),
                FriendData("오수린"),
                FriendData("오수린"),
                FriendData("오수린"),
            )
        )
        friendAdapter.notifyDataSetChanged()
    }

    private fun initFriendFavoriteAdapter(){
        friendAdapter = FriendAdapter()
        binding.rvFavorite.adapter = friendAdapter
        friendAdapter.friendList.addAll(
            listOf(
                FriendData("오수린"),
            )
        )
        friendAdapter.notifyDataSetChanged()
    }

    private fun initFriendRequestAdapter(){
        friendRequestAdapter = FriendRequestAdapter()
        binding.rvFriendRequest.adapter = friendRequestAdapter
        friendRequestAdapter.friendRequestList.addAll(
            listOf(
                FriendData("오수린"),
            )
        )
        friendRequestAdapter.notifyDataSetChanged()
    }

    private fun initClickListener(){
        binding.ivAddFriend.setOnClickListener {
            val nextIntent = Intent(this, AddFriendActivity::class.java)
            startActivity(nextIntent)
        }
    }
}