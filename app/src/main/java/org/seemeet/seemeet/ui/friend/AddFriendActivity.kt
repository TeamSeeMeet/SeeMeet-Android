package org.seemeet.seemeet.ui.friend

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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

        binding.etSearchFriendId.addTextChangedListener {
            if (binding.etSearchFriendId.text.isNullOrBlank()) { //공백일 때
                binding.ivFriendIdRemoveAll.visibility = View.INVISIBLE
            } else {
                binding.ivFriendIdRemoveAll.visibility = View.VISIBLE
                binding.ivFriendIdRemoveAll.setOnClickListener {
                    binding.etSearchFriendId.setText(null)
                }
            }
        }
    }
}