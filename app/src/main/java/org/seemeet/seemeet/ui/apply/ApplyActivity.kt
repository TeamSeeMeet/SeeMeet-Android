package org.seemeet.seemeet.ui.apply

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityApplyBinding

class ApplyActivity : AppCompatActivity() {
    private val binding: ActivityApplyBinding by lazy {
        ActivityApplyBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClickListener()

        val fragment = FirstApplyFragment()
        supportFragmentManager.beginTransaction().add(R.id.container_apply, fragment).commit()

        val username = intent.getStringExtra("username")

        if (username != null) {
            Log.d("********************username", username)
        }
        val bundle = Bundle()
        bundle.putString("name", username)
        bundle.putInt("id", 0)

        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container_apply, fragment)
        supportFragmentManager.beginTransaction().commit()

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