package org.seemeet.seemeet.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
    }

    private fun initClickListener() {
        binding.ivDetailBack.setOnClickListener {
            finish()
        }
    }
}