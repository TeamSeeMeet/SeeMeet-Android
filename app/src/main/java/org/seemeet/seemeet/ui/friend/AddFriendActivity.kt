package org.seemeet.seemeet.ui.friend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.databinding.ActivityAddFriendBinding

class AddFriendActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
    }

    private fun initClickListener(){
        binding.ivFriendAddBack.setOnClickListener {
            finish()
        }
    }
}