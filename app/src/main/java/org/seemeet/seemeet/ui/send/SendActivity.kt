package org.seemeet.seemeet.ui.send

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.InviData
import org.seemeet.seemeet.databinding.ActivitySendBinding
import org.seemeet.seemeet.ui.receive.SendCancelDialogFragment
import org.seemeet.seemeet.ui.receive.SendConfirmDialogFragment
import org.seemeet.seemeet.ui.send.adapter.SendInvitationAdapter
import org.seemeet.seemeet.ui.viewmodel.SendViewModel

class SendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendBinding
    private val viewModel: SendViewModel by viewModels() //위임초기화
    private var choiceInvi : InviData? = null

    //private var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send)
        binding.viewModel = viewModel


        viewModel.setSendInvitationList()

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
                        Log.d("*******************tag", viewModel.inviList.value!![position].time)
                        choiceInvi = viewModel.inviList.value!![position]

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
/*
        tracker = SelectionTracker.Builder<Long>(
            "sendInvitationId",
            binding.rvSendTimelist,
            SendInvitationAdapter.SelectionKeyProvider(binding.rvSendTimelist),
            SendItemDetailsLookup(binding.rvSendTimelist),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SendInvitationAdapter.SelectionPredicate(binding.rvSendTimelist)
        ).build().apply{
            addObserver(object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    //선택이 바뀔 경우.
                }
            })
        }

        sendInviAdapter.tracker = tracker*/


    }

    private fun setListObserver(){
        viewModel.guestList.observe(this, Observer { guestList ->

            viewModel.setGuestCount(guestList.count{ it.isResponse }, guestList.size)

            guestList.forEach{
                binding.cgSendList.addView(Chip(this).apply{
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

        viewModel.inviList.observe(this, Observer { inviList->
            with(binding.rvSendTimelist.adapter as SendInvitationAdapter){
                setInviList(inviList)
            }
        })

    }


    private fun initButtonClick(){

        //맨 아래 취소 버튼
        binding.btnSendCancel.setOnClickListener {
            var dialogView = SendCancelDialogFragment()
            val bundle = Bundle()
            val choice = choiceInvi

            //서버 달 때 고치자. cancel 시에는 초대장 id가 있으면 될듯.
            bundle.putSerializable("choice", choice)
            dialogView.arguments = bundle

            dialogView.setButtonClickListener( object : SendCancelDialogFragment.OnButtonClickListener {
                override fun onCancelNoClicked() {

                }

                override fun onCancelYesClicked() {
                    //여기서 데이터 전송.
                    //위의 cblist에서 flag가 true인 애들 아이디만 골라서 전송해주기.
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
            val responseCnt = viewModel.guestList.value?.count{ it.isResponse}
            val respondent = viewModel.guestList.value?.size
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
                    //위의 cblist에서 flag가 true인 애들 아이디만 골라서 전송해주기.
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