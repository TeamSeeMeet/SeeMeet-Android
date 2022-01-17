package org.seemeet.seemeet.ui.main

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.fonts.FontFamily
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.seemeet.seemeet.ui.notification.NotificationActivity
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ReminderData
import org.seemeet.seemeet.ui.friend.FriendActivity
import org.seemeet.seemeet.databinding.FragmentHomeBinding
import org.seemeet.seemeet.ui.detail.DetailActivity
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        initNaviDrawer()

        // (1) 더미데이터 셋팅 _ 이후 서버통신 시 교체
        viewmodel.setReminderList()
        viewmodel.setHomeMsg()

        // (2) 어뎁터와 옵저버 셋팅
        setReminderAdapter()

        setViewModelObserve()
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

        reminderListAdapter.setItemClickListener(object: ReminderListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.d("****************home_reminder_title_click_position", "${v.id}/${position}")
                val rd : ReminderData = viewmodel.reminderList.value!!.get(position)
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("detail_title", rd.title)
                startActivity(intent)
            }
        })

        binding.rvHomeReminder.adapter = reminderListAdapter
    }

    // 옵저버 _ 위에서 (1)로 데이터 넣을 경우 옵저버가 관찰하다가 업데이트함.
    private fun setViewModelObserve() {
        viewmodel.reminderList.observe(viewLifecycleOwner){
            reminderList-> with(binding.rvHomeReminder.adapter as ReminderListAdapter){
                //어댑터 내에서 notifyDataSetChanged() 해주는 역할의 함수
                setReminder(reminderList)
             }
        }

        viewmodel.homeMsg.observe(viewLifecycleOwner){
            //아싸 오늘은 '친구' 만나는 날이다! 의 경우 친구만 하얀색의 bold, 글씨크기 약간 키우기
            //TODO : 요 아래 내용 빼서 함수로 만들기.
            val word = "친구"
            val start = it.indexOf(word)
            val end = start + word.length

            val ss = SpannableStringBuilder(it)
            ss.setSpan(ForegroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(RelativeSizeSpan(1.2f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tvHomeMsg.text = ss
        }

        //TODO : 메인 홈 일러스트 따로 뷰모델 적용시키는 것 안 했음 << 나중에 나오면 할께요.

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}