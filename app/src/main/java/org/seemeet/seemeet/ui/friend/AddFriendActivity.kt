package org.seemeet.seemeet.ui.friend

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
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

        binding.etSearchFriendId.setOnKeyListener { _, keyCode, event ->
            if ((event.action== KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearchFriendId.windowToken, 0)
            }
            true
        }
    }
}