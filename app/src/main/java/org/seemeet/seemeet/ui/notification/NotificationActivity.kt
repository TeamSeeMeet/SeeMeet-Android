package org.seemeet.seemeet.ui.notification

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityNotificationBinding
import org.seemeet.seemeet.ui.notification.adapter.PagerFragmentStateAdapter
import org.seemeet.seemeet.ui.notification.done.NotiDoneFragment
import org.seemeet.seemeet.ui.notification.inprogress.NotiInProgressFragment

class NotificationActivity : AppCompatActivity() {
    private var _binding : ActivityNotificationBinding? = null
    val binding get() = _binding!!

    private val notiInProgressFragment by lazy { NotiInProgressFragment() }
    private val notiDoneFragment by lazy { NotiDoneFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       _binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTab()
        initClickListener()
    }


    private fun initTab() {
        val pagerAdapter = PagerFragmentStateAdapter(this)
        pagerAdapter.addFragment(NotiInProgressFragment())
        pagerAdapter.addFragment(NotiDoneFragment())

        binding.vpNoti.adapter = pagerAdapter

        TabLayoutMediator(binding.tlNoti, binding.vpNoti) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = getString(R.string.noti_inprogress)
                }
                1 -> {
                    tab.text = getString(R.string.noti_done)
                }
            }
        }.attach()
    }
    private fun initClickListener(){
        binding.ivNotiTopBack.setOnClickListener {
            Log.d("************notiback", "press back")
        }
    }



    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }
}