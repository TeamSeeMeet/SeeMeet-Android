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
import com.bumptech.glide.Glide
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.SeeMeetSharedPreference.getLogin
import org.seemeet.seemeet.data.SeeMeetSharedPreference.getToken
import org.seemeet.seemeet.databinding.FragmentHomeBinding
import org.seemeet.seemeet.ui.detail.DetailActivity
import org.seemeet.seemeet.ui.friend.FriendActivity
import org.seemeet.seemeet.ui.main.MainActivity
import org.seemeet.seemeet.ui.main.home.adapter.ReminderListAdapter
import org.seemeet.seemeet.ui.mypage.MyPageActivity
import org.seemeet.seemeet.ui.notification.NotificationActivity
import org.seemeet.seemeet.ui.registration.LoginMainActivity
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.HomeViewModel
import org.seemeet.seemeet.util.CustomToast
import org.seemeet.seemeet.util.calDday
import org.seemeet.seemeet.util.getStatusBarHeight
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
        setPushOption()

        binding.clHomeTop.setPadding(0, getStatusBarHeight(requireContext()), 0, 0)
        binding.nvMypage.tvMypageLogin.text = SeeMeetSharedPreference.getUserName()
        binding.nvMypage.tvEmail.text = SeeMeetSharedPreference.getUserEmail()

        if(getLogin()) {
            viewmodel.requestFriendList()
            viewmodel.requestComePlanList()
            viewmodel.requestLastPlanData()
            setViewVisible(binding.clHomeNoReminder, false)
        } else {
            setHomeBanner(-1)
            setViewVisible(binding.clHomeNoReminder, true)
        }

        setViewVisible(binding.clErrorNetwork, false)
        setViewVisible(binding.rvHomeReminder, true)

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

            if(SeeMeetSharedPreference.getUserProfile().isNullOrEmpty()){
                Glide.with(activity!!).load(R.drawable.ic_img_profile).circleCrop()
                    .into(nvMypage.ivMypageProfile)
            }else{
                Glide.with(activity!!).load(SeeMeetSharedPreference.getUserProfile()).circleCrop()
                .into(nvMypage.ivMypageProfile)
            }

            nvMypage.clMypageLogin.setOnClickListener{
                if(!getLogin())
                    LoginMainActivity.start(requireContext())
                else MyPageActivity.start(requireContext())
            }

            nvMypage.clMypageContent.setOnClickListener {
                CustomToast.createToast(requireContext(), "아직 준비중인 서비스예요")?.show()
            }

            nvMypage.swPush.setOnClickListener {
                changePushOption()
            }
        }

    }

    private fun setPushOption(){
        binding.nvMypage.swPush.isChecked = SeeMeetSharedPreference.getPushOn()
    }

    private fun changePushOption(){
        if(binding.nvMypage.swPush.isChecked) {
            SeeMeetSharedPreference.setPushOn(true)
            viewmodel.setPushNotification(true, getToken())
        }else{
            SeeMeetSharedPreference.setPushOn(false)
            viewmodel.setPushNotification(false, getToken())
        }
    }

    private fun initNaviDrawer(){

        val toggle = ActionBarDrawerToggle(
            this.activity, binding.dlHomeMypage, R.string.home_drawer_open, R.string.home_drawer_close
        )
        binding.dlHomeMypage.setStatusBarBackground(R.color.gray06)
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
                    setViewVisible(binding.clHomeNoReminder, true)
                } else {
                    setViewVisible(binding.clHomeNoReminder, false)
                }
            }
        }

        viewmodel.lastPlan.observe(viewLifecycleOwner){
            lastPlan ->
                if(lastPlan.date.isNullOrEmpty()){
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
                    val error = (it.first as HttpException)
                    message = "${error.code()} ERROR : \n ${error.response()!!.errorBody()!!.string().split("\"")[7]}"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    setViewVisible(binding.clErrorNetwork, true)
                    setViewVisible(binding.rvHomeReminder, false)
                }
                else ->  {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }

            }

            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())

            if(message != "") {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                setViewVisible(binding.clHomeNoReminder, true)
            }
        }

    }

    private fun setViewVisible(view: View, flag : Boolean){
         if(flag){
            view.visibility = View.VISIBLE
         } else {
             view.visibility = View.INVISIBLE
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
        binding.nvMypage.tvEmail.text = SeeMeetSharedPreference.getUserId()

        if(getLogin()) {
            viewmodel.requestFriendList()
            viewmodel.requestComePlanList()
            viewmodel.requestLastPlanData()
            setViewVisible(binding.clHomeNoReminder, false)
        } else {
            setHomeBanner(-1)
            setViewVisible(binding.clHomeNoReminder, true)
        }

        setViewVisible(binding.clErrorNetwork, false)
        setViewVisible(binding.rvHomeReminder, true)

    }

}