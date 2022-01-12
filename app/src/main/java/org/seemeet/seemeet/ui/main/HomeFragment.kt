package org.seemeet.seemeet.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.seemeet.seemeet.ui.notification.NotificationActivity
import org.seemeet.seemeet.R
import org.seemeet.seemeet.ui.friend.FriendActivity
import org.seemeet.seemeet.databinding.FragmentHomeBinding
import org.seemeet.seemeet.ui.main.adapter.ReminderListAdapter
import org.seemeet.seemeet.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!

    private val viewmodel : HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        initNaviDrawer()

        // (1) 더미데이터 셋팅 _ 이후 서버통신 시 교체
        viewmodel.setReminderList()

        // (2) 어뎁터와 옵저버 셋팅
        setReminderAdapter()
        setReminderObserve()
    }


    private fun initClickListener(){

        binding.apply{
            ivHomeFriend.setOnClickListener {
                FriendActivity.start(requireContext())
            }
            ivHomeNoti.setOnClickListener {
                NotificationActivity.start(requireContext())
            }

            ivMypageMenu.setOnClickListener{
                binding.dlHomeMypage.openDrawer(GravityCompat.START)
            }
            nvMypage.ivMypageBack.setOnClickListener {
                binding.dlHomeMypage.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun initNaviDrawer(){
        val toggle = ActionBarDrawerToggle(
            this.activity, binding.dlHomeMypage, R.string.home_drawer_open, R.string.home_drawer_close
        )

        binding.dlHomeMypage.addDrawerListener(toggle)
        toggle.syncState()

    }

    // 어댑터
    private fun setReminderAdapter() {
        val reminderListAdapter = ReminderListAdapter()

        binding.rvHomeReminder.adapter = reminderListAdapter
    }

    // 옵저버 _ 위에서 (1)로 데이터 넣을 경우 옵저버가 관찰하다가 업데이트함.
    private fun setReminderObserve() {
        viewmodel.reminderList.observe(viewLifecycleOwner){
            reminderList-> with(binding.rvHomeReminder.adapter as ReminderListAdapter){
                //어댑터 내에서 notifyDataSetChanged() 해주는 역할의 함수
                setReminder(reminderList)
             }
        }

    }
}