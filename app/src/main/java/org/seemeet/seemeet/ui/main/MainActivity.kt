package org.seemeet.seemeet.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.ActivityMainBinding
import org.seemeet.seemeet.ui.apply.ApplyActivity
import org.seemeet.seemeet.ui.main.calendar.CalendarFragment
import org.seemeet.seemeet.ui.main.home.HomeFragment
import org.seemeet.seemeet.ui.receive.DialogHomeNoLoginFragment
import org.seemeet.seemeet.ui.registration.LoginActivity
import org.seemeet.seemeet.ui.viewmodel.HomeViewModel
import org.seemeet.seemeet.util.CustomToast
import org.seemeet.seemeet.util.getNaviBarHeight

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val homeFragment by lazy { HomeFragment() }
    private val calendarFragment by lazy { CalendarFragment() }

    //뒤로가기 연속 클릭 대기 시간
    var mBackWait:Long = 0
    var friendCnt : Int = 0
    private val viewmodel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNavigation()

        setFriendListObserver()
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.clMainLayout.setPadding(0,0,0, getNaviBarHeight(this))
    }

    private fun setBottomNavigation() {
        //가운데 플로팅 버튼 클릭 시
        binding.fabMain.setOnClickListener {

            if(SeeMeetSharedPreference.getLogin() && friendCnt != 0)
                ApplyActivity.start(this)
            else if (!SeeMeetSharedPreference.getLogin()){
                setNoLoginDailog()
            } else {
                val toast = Toast.makeText(applicationContext, "친구를 먼저 추가해보세요", Toast.LENGTH_LONG)
                toast.show()
            }

        }

        //양 옆 네비게이션 버튼 클릭 시
        binding.bnvMain.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> changeFragment(getString(R.string.label_home))
                    //R.id.menu_apply -> ApplyActivity.start(this)
                    R.id.menu_calendar -> changeFragment(getString(R.string.label_calendar))
                }
                true
            }
        changeFragment(getString(R.string.label_home))
    }

    private fun changeFragment(tag: String) {
        val fm = supportFragmentManager
        val fragment = fm.findFragmentByTag(tag) ?: when (tag) {
            getString(R.string.label_home) -> homeFragment
            getString(R.string.label_calendar) -> calendarFragment
            else -> null
        } ?: return

        if (fragment.isVisible) return
        val transaction = fm.beginTransaction()

        fm.fragments
            .filter { it.isVisible }
            .forEach {
                transaction.hide(it)
            }

        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.fcv_main, fragment, tag)
        }

        transaction.commit()
    }

    private fun setNoLoginDailog(){

        val dialogView = DialogHomeNoLoginFragment()
        val context = this
        dialogView.setButtonClickListener( object :  DialogHomeNoLoginFragment.OnButtonClickListener {
            override fun onCancelClicked() {

            }

            override fun onLoginClicked() {
                LoginActivity.start(context)
            }

        })
        dialogView.show(supportFragmentManager, "send wish checkbox time")

    }

    private fun setFriendListObserver() {
        viewmodel.friendList.observe(this){
                friendList ->
            Log.d("***********HOME_FIREND_COUNT2", friendList.data.size.toString())
            friendCnt = friendList.data.size
        }
    }

    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait < 2000 ) {
            finish()
            return
        } else {
            mBackWait = System.currentTimeMillis()
            CustomToast.createToast(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.")?.show()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}