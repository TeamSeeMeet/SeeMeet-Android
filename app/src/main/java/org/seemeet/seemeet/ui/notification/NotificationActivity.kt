package org.seemeet.seemeet.ui.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityNotificationBinding
import org.seemeet.seemeet.ui.notification.adapter.PagerFragmentStateAdapter

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
        initViewPager()
    }

    private fun initTab() {
        val pagerAdapter = PagerFragmentStateAdapter(this)
        pagerAdapter.addFragment(NotiInProgressFragment())
        pagerAdapter.addFragment(NotiDoneFragment())

        binding.vpNoti.adapter = pagerAdapter

        TabLayoutMediator(binding.tlNoti, binding.vpNoti) { tab, position ->

            when (position) {
                0 -> {
                    tab.text = getString(R.string.noti_in_progress)
                }
                1 -> {
                    tab.text = getString(R.string.noti_done)
                }
            }
        }.attach()

        fun TabLayout.changeTabsFont(selectPosition: Int) {
            val vg = this.getChildAt(0) as ViewGroup
            val tabsCount = vg.childCount
            for (j in 0 until tabsCount) {
                val vgTab = vg.getChildAt(j) as ViewGroup
                vgTab.forEachIndexed { index, _ ->
                    val tabViewChild = vgTab.getChildAt(index)
                    if (tabViewChild is TextView) {
                        tabViewChild.setTextBold(j == selectPosition)
                    }
                }
            }
        }

        val tabLayoutOnPageChangeListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tabItem: TabLayout.Tab?) {}

            override fun onTabUnselected(tabItem: TabLayout.Tab?) {}

            override fun onTabSelected(tabItem: TabLayout.Tab?) {
                tabItem?.position?.let {
                    binding.tlNoti.changeTabsFont(it)
                }
            }
        }

        binding.tlNoti.addOnTabSelectedListener(tabLayoutOnPageChangeListener)
        binding.tlNoti.changeTabsFont(0)

    }

    private fun initClickListener(){
        binding.ivNotiTopBack.setOnClickListener {
            Log.d("************notiback", "press back")
        }
    }

    private fun initViewPager(){
        binding.vpNoti.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    fun TextView.setTextBold(isBold: Boolean) {
        this.typeface = ResourcesCompat.getFont(this.context,if(isBold) R.font.spoqa_han_sans_neo_bold else R.font.spoqa_han_sans_neo_medium)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }
}