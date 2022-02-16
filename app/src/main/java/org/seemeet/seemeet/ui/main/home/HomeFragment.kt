package org.seemeet.seemeet.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.SeeMeetSharedPreference.getLogin
import org.seemeet.seemeet.databinding.FragmentHomeBinding
import org.seemeet.seemeet.ui.detail.DetailActivity
import org.seemeet.seemeet.ui.friend.FriendActivity
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.main.home.adapter.ReminderListAdapter
import org.seemeet.seemeet.ui.notification.NotificationActivity
import org.seemeet.seemeet.ui.registration.LoginActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.HomeViewModel
import org.seemeet.seemeet.util.calDday
import org.seemeet.seemeet.util.changeStatusBarColor
import org.seemeet.seemeet.util.setBetweenDays2
import retrofit2.HttpException

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!

    private val viewmodel : HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewmodel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        initNaviDrawer()

        binding.nvMypage.tvMypageLogin.text = SeeMeetSharedPreference.getUserName()
        binding.nvMypage.tvEmail.text = SeeMeetSharedPreference.getUserEmail()

        if(getLogin()) {
            viewmodel.requestFriendList()
            viewmodel.requestComePlanList()
            viewmodel.requestLastPlanData()
            setNoReminderListMsgVisible(false)
        } else {
            setHomeBanner(-1)
            setNoReminderListMsgVisible(true)
        }

        setReminderAdapter()
        setViewModelObserve()

        //해당 뷰 status bar 색상 변경
        changeStatusBarColor(R.color.pink01, requireActivity(), requireContext())
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

            nvMypage.clMypageLogin.setOnClickListener{
                if(!getLogin())
                    LoginActivity.start(requireContext())
            }

            nvMypage.clMypageContent.setOnClickListener {
                Toast.makeText(requireContext(), "아직 준비중인 서비스예요",Toast.LENGTH_SHORT
                ).show()
            }

            nvMypage.tvEmail.setOnClickListener {
                SeeMeetSharedPreference.clearStorage()
                LoginActivity.start(requireContext())
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
                viewmodel.comePlanList.value?.let{ listData ->
                    val comePlanId = listData.data.filter{it.date.setBetweenDays2() <= 0}[position].planId
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("planId", comePlanId)
                    startActivity(intent)
                }
            }
        })

        binding.rvHomeReminder.adapter = reminderListAdapter
    }

    // 옵저버 _ 위에서 (1)로 데이터 넣을 경우 옵저버가 관찰하다가 업데이트함.
    private fun setViewModelObserve() {

        viewmodel.comePlanList.observe(viewLifecycleOwner) { comePlanList ->
            with(binding.rvHomeReminder.adapter as ReminderListAdapter) {

                val comePlan = comePlanList.data.filter { it.date.setBetweenDays2() <= 0 }
                setReminder(comePlan)

                if(comePlan.isEmpty()) {
                    setNoReminderListMsgVisible(true)
                } else {
                    setNoReminderListMsgVisible(false)
                }
            }
        }

        viewmodel.lastPlan.observe(viewLifecycleOwner){
            lastPlan ->
                if(lastPlan.date.isEmpty()){
                    setHomeBanner(-1)
                } else {
                    setHomeBanner(lastPlan.date.calDday())
                }
        }

        viewmodel.fetchState.observe(viewLifecycleOwner){
            var message = ""
            when( it.second){
                BaseViewModel.FetchState.BAD_INTERNET-> {
                    message = "소켓 오류 / 서버와 연결에 실패하였습니다."
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                   val code = (it.first as HttpException).code()
                    message = "$code ERROR : \n ${it.first.message}"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    message = "호스트를 확인할 수 없습니다. 네트워크 연결을 확인해주세요"
                }
                else ->  {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }

            }

            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            setNoReminderListMsgVisible(true)
        }

    }

    private fun setNoReminderListMsgVisible(flag : Boolean){
         if(flag){
             binding.clHomeNoReminder.visibility = View.VISIBLE
         } else {
             binding.clHomeNoReminder.visibility = View.INVISIBLE
         }
    }

    private fun setHomeBanner(day : Int) {

        var flag: Int

        if(day == -1 ){
            flag = if(getLogin() && MainActivity().friendCnt != 0 ) 2
                    else 1
        } else {
            flag = when(day){
                0 -> 3
                in 1..14 -> 4
                in 15..21 -> 5
                else -> 6
            }

            if((1..2).random() == 2){
                flag = 2
            }
        }

        Log.d("**********HOME_BANNER_FRAGMENT", "$flag, $day")
        viewmodel.setHomeBannerFlagAndDay(flag, day)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        binding.nvMypage.tvMypageLogin.text = SeeMeetSharedPreference.getUserName()
        binding.nvMypage.tvEmail.text = SeeMeetSharedPreference.getUserEmail()

        if(getLogin()) {
            viewmodel.requestFriendList()
            viewmodel.requestComePlanList()
            viewmodel.requestLastPlanData()
            setNoReminderListMsgVisible(false)
        } else {
            setHomeBanner(-1)
            setNoReminderListMsgVisible(true)
        }
    }

}