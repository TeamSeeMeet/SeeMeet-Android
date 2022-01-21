package org.seemeet.seemeet.ui.apply

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.databinding.ActivityApplyBinding

class ApplyActivity : AppCompatActivity() {
    private val binding: ActivityApplyBinding by lazy {
        ActivityApplyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()

        val username = intent.getStringExtra("username")

        if (username != null) {
            Log.d("********************username", username)
        }
    }

    private fun initClickListener() {
        binding.ivX.setOnClickListener {
            finish()
        }
    }
/*
    override fun onBackPressed() {
        super.onBackPressed()
    }*/

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApplyActivity::class.java)
            context.startActivity(intent)
        }
    }
}