package org.seemeet.seemeet.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.seemeet.seemeet.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var text01 : String? = intent?.getStringExtra("detail_title")

        if (text01 != null) {
            Log.d("*****************Detail", text01)
        }
    }
}