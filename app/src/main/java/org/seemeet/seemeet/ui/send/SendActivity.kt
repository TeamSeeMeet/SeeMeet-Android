package org.seemeet.seemeet.ui.send

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.local.InviData
import org.seemeet.seemeet.data.model.response.invitation.SendInvitationDate
import org.seemeet.seemeet.databinding.ActivitySendBinding
import org.seemeet.seemeet.ui.receive.SendCancelDialogFragment
import org.seemeet.seemeet.ui.receive.SendConfirmDialogFragment
import org.seemeet.seemeet.ui.send.adapter.SendInvitationAdapter
import org.seemeet.seemeet.ui.viewmodel.SendViewModel

class SendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendBinding
    private val viewModel: SendViewModel by viewModels() //위임초기화
    private var choiceInvi : SendInvitationDate? = null
    private var invitationId = -1

    //private var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        invitationId =  intent.getIntExtra("invitationId", -1)
        
        if(invitationId != -1){
            Log.d("********SEND_INVITATION_ID", invitationId.toString())
            viewModel.requestSendInvitationData(invitationId)
        }

        setInviAdapter()
        setListObserver()

        setSingleChoiceTime()
        initButtonClick()

    }

    private fun setSingleChoiceTime(){
        binding.rvSendTimelist.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener{
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if(e.action != MotionEvent.ACTION_MOVE){
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if(child != null){
                        val position = rv.getChildAdapterPosition(child)
                        val view = rv.layoutManager?.findViewByPosition(position)

                        view?.isActivated = true
                        Log.d("*******************tag", viewModel.sendInvitationDateList.value!![position].start)
                        choiceInvi = viewModel.sendInvitationDateList.value!![position]

                        binding.btnSendDecide.isEnabled = true

                        binding.btnSendDecide.setBackground(resources.getDrawable(R.drawable.rectangle_pink01_10))
                        for(i in 0..rv.adapter!!.itemCount){
                            val otherView = rv.layoutManager?.findViewByPosition(i)
                            if(otherView != view){
                                otherView?.isActivated = false
                            }
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                TODO("Not yet implemented")
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setInviAdapter() {
        val sendInviAdapter = SendInvitationAdapter()

        binding.rvSendTimelist.adapter = sendInviAdapter

    }

    private fun setListObserver(){
        viewModel.sendInvitation.observe(this, Observer {
            sendInvitation ->
            viewModel.setSendInvitationData()
            viewModel.setSendInvitationDateList()
        })

        viewModel.sendInvitationData.observe(this, Observer {
            invitation ->

            val word = invitation.guests.count{it.isResponse}.toString() + "/" + invitation.guests.size.toString()
            val start = 0
            val end = 1

            val ss = SpannableStringBuilder(word)
            ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(ForegroundColorSpan(Color.parseColor("#FA555C")), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            binding.tvSendCount.text = ss

            invitation.guests.forEach{
                binding.cgSendList.addView(Chip(this).apply {
                    text = it.username
                    if(it.isResponse){
                        setChipBackgroundColorResource(R.color.pink01)
                        setTextAppearance(R.style.chipTextWhiteStyle)
                        isCheckable = false
                        isEnabled = false

                    } else {
                        setChipBackgroundColorResource(R.color.white)
                        setTextAppearance(R.style.chipTextPinkStyle)
                        chipStrokeWidth = 1.0F
                        setChipStrokeColorResource(R.color.pink01)
                        isCheckable = false
                        isEnabled = false
                    }
                })
            }
        })

        viewModel.sendInvitationDateList.observe(this, Observer {
            dateList -> with(binding.rvSendTimelist.adapter as SendInvitationAdapter){
                setInviList(dateList)
            }
        })

    }


    private fun initButtonClick(){

        //맨 아래 취소 버튼
        binding.btnSendCancel.setOnClickListener {
            var dialogView = SendCancelDialogFragment()

            //서버 달 때 고치자. cancel 시에는 초대장 id가 있으면 될듯.

            dialogView.setButtonClickListener( object : SendCancelDialogFragment.OnButtonClickListener {
                override fun onCancelNoClicked() {

                }

                override fun onCancelYesClicked() {
                    if (invitationId != -1) {
                        viewModel.requestSendCancelInvitation(invitationId)
                        finish()
                    }
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }

        //맨 아래 수락 버튼
        binding.btnSendDecide.setOnClickListener {
            var dialogView = SendConfirmDialogFragment()
            val bundle = Bundle()
            val choice = choiceInvi

            //서버 달 때 고치자. cancel 시에는 초대장 id가 있으면 될듯.
            bundle.putSerializable("choice", choice)

            //확정 시 상태에 따른 메세지 내용 값 1. 전부 답변 & 만장일치 선택. 2. 전부 답변, 만장일치x 3. 전부 답변 x
            val responseCnt = viewModel.sendInvitationData.value?.guests?.count{it.isResponse}
            val respondent = viewModel.sendInvitationData.value?.guests?.size
            val choiceCnt = choice?.respondent?.size

            Log.d("*************수락버튼 클릭 시 차래로 rcn, rpt, cct", "$responseCnt, $respondent, $choiceCnt")

            if(responseCnt != respondent){
                bundle.putInt("check", 3)
            } else if (responseCnt != choiceCnt){
                bundle.putInt("check", 2)
            } else {
                bundle.putInt("check", 1)
            }

            dialogView.arguments = bundle

            dialogView.setButtonClickListener( object : SendConfirmDialogFragment.OnButtonClickListener {
                override fun onConfirmNoClicked() {

                }

                override fun onConfirmYesClicked() {
                    //여기서 데이터 전송.
                    if (choice != null) {
                        viewModel.requestSendConfirmInvitation(choice.invitationId, choice.id )
                        finish()
                    }
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }

        binding.ivSendBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SendActivity::class.java)
            context.startActivity(intent)
        }
    }
}