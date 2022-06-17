package org.seemeet.seemeet.ui.send

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.invitation.SendInvitationDate
import org.seemeet.seemeet.databinding.ActivitySendBinding
import org.seemeet.seemeet.ui.receive.SendCancelDialogFragment
import org.seemeet.seemeet.ui.receive.SendConfirmDialogFragment
import org.seemeet.seemeet.ui.send.adapter.SendInvitationAdapter
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.SendViewModel
import retrofit2.HttpException

class SendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendBinding
    private val viewModel: SendViewModel by viewModels() //위임초기화
    private var choiceInvi : SendInvitationDate? = null // 선택한 시간대 객체
    private var invitationId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        invitationId =  intent.getIntExtra("invitationId", -1)

        setViewVisible(binding.nsvSend, true)
        setViewVisible(binding.clErrorNetwork, false)

        if(invitationId != -1){
            Log.d("********SEND_INVITATION_ID", invitationId.toString())
            viewModel.requestSendInvitationData(invitationId)
            setInviAdapter()
            setListObserver()

            setSingleChoiceTime()
            initButtonClick()
        }

    }

    //시간 선택지 _ recyclerView 아이템 단일 선택하기.
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
                        choiceInvi = viewModel.sendInvitationDateList.value?.get(position)

                        if(!binding.btnSendDecide.isEnabled) {
                            binding.btnSendDecide.isEnabled = true
                            binding.btnSendDecide.background = ResourcesCompat.getDrawable(
                                resources, R.drawable.rectangle_pink01_10, null
                            )
                        }

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

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        })
    }

    private fun setInviAdapter() {
        val sendInviAdapter = SendInvitationAdapter()

        binding.rvSendTimelist.adapter = sendInviAdapter
    }

    private fun setListObserver(){
        viewModel.sendInvitationData.observe(this) { invitation ->

            //초대장 받은 사람들에 대한 chip 그리기
            if(invitation.guests[0].id != -1) {
                invitation.guests.forEach {
                    binding.cgSendList.addView(Chip(this).apply {
                        text = it.username
                        if (it.isResponse) {
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
            }
        }

        viewModel.sendInvitationDateList.observe(this) {
            dateList -> with(binding.rvSendTimelist.adapter as SendInvitationAdapter){
                setInviList(dateList)
            }
        }

        viewModel.fetchState.observe(this){
            val message: String
            when( it.second){
                BaseViewModel.FetchState.BAD_INTERNET-> {
                    message = "소켓 오류 / 서버와 연결에 실패하였습니다."
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val error = (it.first as HttpException)
                    message = "${error.code()} ERROR : \n ${error.response()!!.errorBody()!!.string().split("\"")[7]}"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    setViewVisible(binding.nsvSend, false)
                    setViewVisible(binding.clErrorNetwork, true)
                }
                else ->  {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }

            }

            if(message != "") {
                Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setViewVisible(view: View, flag : Boolean){
        if(flag){
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    private fun initButtonClick(){

        //맨 아래 취소 버튼 관련 팝업 띄우기
        binding.btnSendCancel.setOnClickListener {
            val dialogView = SendCancelDialogFragment()

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

        //맨 아래 수락 버튼 관련 팝업 띄우기
        binding.btnSendDecide.setOnClickListener {
            val dialogView = SendConfirmDialogFragment()
            val bundle = Bundle()

            bundle.putSerializable("choice", choiceInvi)

            //확정 시 상태에 따른 메세지 내용 값 1. 전부 답변 & 만장일치 선택. 2. 전부 답변, 만장일치x 3. 전부 답변 x
            val responseCnt = viewModel.sendInvitationData.value?.guests?.count{it.isResponse}
            val respondent = viewModel.sendInvitationData.value?.guests?.size
            val choiceCnt = choiceInvi?.respondent?.size

            when(true){
                (responseCnt != respondent) -> bundle.putInt("check", 3)
                (responseCnt != choiceCnt) ->  bundle.putInt("check", 2)
                else -> bundle.putInt("check", 1)
            }

            dialogView.arguments = bundle

            dialogView.setButtonClickListener( object : SendConfirmDialogFragment.OnButtonClickListener {
                override fun onConfirmNoClicked() {

                }

                override fun onConfirmYesClicked() {
                    //여기서 데이터 전송.
                    if (choiceInvi != null) {
                        viewModel.requestSendConfirmInvitation(choiceInvi!!.invitationId, choiceInvi!!.id )
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

    override fun onResume() {
        super.onResume()
        setViewVisible(binding.nsvSend, true)
        setViewVisible(binding.clErrorNetwork, false)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SendActivity::class.java)
            context.startActivity(intent)
        }
    }
}