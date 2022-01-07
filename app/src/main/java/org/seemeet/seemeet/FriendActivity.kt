package org.seemeet.seemeet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FriendActivity::class.java)
            context.startActivity(intent)
        }
    }
}