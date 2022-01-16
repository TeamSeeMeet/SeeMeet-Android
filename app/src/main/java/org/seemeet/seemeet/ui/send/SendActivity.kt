package org.seemeet.seemeet.ui.send

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityReceiveBinding
import org.seemeet.seemeet.databinding.ActivitySendBinding
import org.seemeet.seemeet.ui.viewmodel.ReceiveViewModel

class SendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendBinding
   // private val viewModel: SendViewModel by viewModels() //위임초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receive)

    }
}