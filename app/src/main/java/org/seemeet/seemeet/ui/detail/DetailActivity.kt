package org.seemeet.seemeet.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.ActivityDetailBinding
import org.seemeet.seemeet.ui.viewmodel.DetailViewModel
import org.seemeet.seemeet.util.BindingAdapters

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels() //위임초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 넘어온 planId 가지고 서버랑 통신하기.
        val planId = intent.getIntExtra("planId", -1)
        Log.d("*************DETAIL_PLANID", planId.toString())

        viewModel.requestPlanId(planId)
        initClickListener()
        setListObserver()
    }

    private fun setListObserver() {
        viewModel.planDetail.observe(this, Observer { planDetail ->

            planDetail.forEach {
                BindingAdapters.setYearMonthDate(binding.tvDetailDay, it.data.date)
                binding.tvDetailTitle.text = it.data.invitationTitle
                BindingAdapters.setStartEndTimeText(binding.tvDetailTime, it.data.start, it.data.end)
                binding.tvRecieveLetterTitle.text = it.data.invitationTitle
                binding.tvRecieveLetterContent.text = it.data.invitationDesc
                binding.tvDetailHostName.text = it.data.hostname

                it.data.possible.forEach {
                    binding.cgDetailFriendList.addView(Chip(this).apply {
                        text = it.username
                        setChipBackgroundColorResource(R.color.pink01)
                        setTextAppearance(R.style.chipTextWhiteStyle2)
                        isCheckable = false
                        isEnabled = false
                    })
                }
                it.data.impossible.forEach {
                    binding.cgDetailFriendList.addView(Chip(this).apply {
                        text = it.username
                        setChipBackgroundColorResource(R.color.gray04)
                        setTextAppearance(R.style.chipTextWhiteStyle2)
                        isCheckable = false
                        isEnabled = false
                    })
                }
            }
        })
    }

    private fun initClickListener() {
        binding.ivDetailBack.setOnClickListener {
            finish()
        }

/* 약속 상세에서 취소는 일단 지움
        binding.btnAppointmentCancel.setOnClickListener {
            var dialogView = DetailDialogFragment()
            val bundle = Bundle()

            //서버 달 때 고치자. cancel 시에는 초대장 id가 있으면 될듯.

            dialogView.arguments = bundle

            dialogView.setButtonClickListener( object :  DetailDialogFragment.OnButtonClickListener {
                override fun onCancelNoClicked() {

                }

                override fun onCancelYesClicked() {
                    //Toast.makeText(this@DetailActivity, "약속이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                    //여기서 데이터 전송.
                    //위의 cblist에서 flag가 true인 애들 아이디만 골라서 전송해주기.
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }
 */
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}