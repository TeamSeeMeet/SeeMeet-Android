package org.seemeet.seemeet.ui.main


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.ActivityMainBinding
import org.seemeet.seemeet.ui.apply.ApplyActivity
import org.seemeet.seemeet.ui.main.calendar.CalendarFragment
import org.seemeet.seemeet.ui.main.home.HomeFragment
import org.seemeet.seemeet.ui.mypage.MyPageActivity
import org.seemeet.seemeet.ui.receive.DialogHomeNoLoginFragment
import org.seemeet.seemeet.ui.registration.LoginMainActivity
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
    var mBackWait: Long = 0
    var friendCnt: Int = 0
    private val viewmodel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNavigation()
        setFriendListObserver()

        initView()
    }

    override fun onResume() {
        super.onResume()
        initCheck()
    }

    fun initCheck() {
        //이메일로 가입 시 이름, 아이디 설정 안했을 경우 다이얼로그 띄우기
        if (SeeMeetSharedPreference.getUserName() == "0") {
            val dialogView = DialogGotoMyPageFragment()
            val bundle = Bundle()

            dialogView.arguments = bundle

            dialogView.setButtonClickListener(object :
                DialogGotoMyPageFragment.OnButtonClickListener {
                override fun onGotoMyPageClicked() {
                    val intent = Intent(this@MainActivity, MyPageActivity::class.java)
                    intent.putExtra("NoNameId", true)
                    this@MainActivity.startActivity(intent)
                }
            })
            //다이얼로그 바깥 영역 눌렀을 떄 dismiss 막기
            dialogView.isCancelable = false
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }
    }

    private fun initView() {
        //디바이스 크기에 딱 맞게 하기. (statusbar, navibar 높이 포함 _ 투명 statusbar를 위해)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.clMainLayout.setPadding(0, 0, 0, getNaviBarHeight(this))

        //바텀 네비 높이 만큼 FragmentContainerView의 bottom에 마진 주기.
        binding.bnvMain.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val height = binding.bnvMain.measuredHeight

        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(0, 0, 0, height)
        binding.fcvMain.layoutParams = layoutParams
    }

    private fun setBottomNavigation() {
        //가운데 플로팅 버튼 클릭 시
        binding.fabMain.setOnClickListener {

            if (SeeMeetSharedPreference.getLogin() && friendCnt != 0)
                ApplyActivity.start(this)
            else if (!SeeMeetSharedPreference.getLogin()) {
                setNoLoginDailog()
            } else {
                CustomToast.createToast(applicationContext, "친구를 먼저 추가해보세요")?.show()
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

    private fun setNoLoginDailog() {

        val dialogView = DialogHomeNoLoginFragment()
        val context = this
        dialogView.setButtonClickListener(object : DialogHomeNoLoginFragment.OnButtonClickListener {
            override fun onCancelClicked() {

            }

            override fun onLoginClicked() {
                LoginMainActivity.start(context)
            }

        })
        dialogView.show(supportFragmentManager, "send wish checkbox time")

    }

    private fun setFriendListObserver() {
        viewmodel.friendList.observe(this) { friendList ->
            Log.d("***********HOME_FRIEND_COUNT2", friendList.data.size.toString())
            friendCnt = friendList.data.size
        }
    }

    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if (System.currentTimeMillis() - mBackWait < 2000) {
            finish()
            return
        } else {
            mBackWait = System.currentTimeMillis()
            CustomToast.createToast(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.")?.show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("************main_newIntent1", intent.toString())
        if (intent != null) {
            val push = intent.getBooleanExtra("pushPlan", false)
            Log.d("************main_newIntent2", push.toString())
            if (push) {
                binding.bnvMain.menu.getItem(2).isChecked = true
                val bundle = Bundle()
                bundle.putBoolean("tomorrow", true)

                val calFragment = CalendarFragment()
                calFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_main, calFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}