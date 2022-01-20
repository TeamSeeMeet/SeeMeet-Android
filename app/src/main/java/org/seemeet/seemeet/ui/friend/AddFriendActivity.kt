package org.seemeet.seemeet.ui.friend

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import org.seemeet.seemeet.data.local.FriendIdData
import org.seemeet.seemeet.databinding.ActivityAddFriendBinding
import org.seemeet.seemeet.ui.viewmodel.AddFriendViewModel

class AddFriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFriendBinding
    private val viewModel: AddFriendViewModel by viewModels()
    private var data = mutableListOf<FriendIdData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.returnAddFriendList()

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

    private fun initClickListener() {
        binding.ivFriendAddBack.setOnClickListener {
            finish()
        }

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

        binding.ivAddFriendList.setOnClickListener {
            binding.ivAddFriendList.isSelected = binding.ivAddFriendList.isSelected != true
        }

    }
}