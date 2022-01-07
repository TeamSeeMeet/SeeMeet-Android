package org.seemeet.seemeet.ui.apply

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.R

class ApplyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApplyActivity::class.java)
            context.startActivity(intent)
        }
    }
}