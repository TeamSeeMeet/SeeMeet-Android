package org.seemeet.seemeet.ui.receive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
import org.seemeet.seemeet.databinding.ActivityReceiveBinding
import org.seemeet.seemeet.ui.receive.adapter.ReceiveCheckListAdapter
import org.seemeet.seemeet.ui.receive.adapter.ReceiveSchduleListAdapter
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.ReceiveViewModel
import retrofit2.HttpException


class ReceiveActivity : AppCompatActivity() {

    private lateinit var binding:  ActivityReceiveBinding
    private val viewModel: ReceiveViewModel by viewModels() //위임초기화
    private var invitationId = -1
    private var isResponse = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receive)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        invitationId = intent.getIntExtra("invitationId", -1)
        Log.d("********RECEIVE_INVITATION_ID", invitationId.toString())

        if (invitationId != -1) {
            viewModel.requestReceiveInvitation(invitationId)

            setCheckBoxAdapter()
            setClickedAdapter()

            setListObserver()

            initButtonClick()
        }

    }

    //리싸이클러 뷰 단일 선택 용 함수
    private fun setSingleChoice(){

        //체크박스 있는 리사이클러뷰 클릭 시  _ 아래에 있는 일정 리마인더에 데이터가 셋팅됨.
        binding.rvReceiveCheckbox.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener{
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if(e.action != MotionEvent.ACTION_MOVE){
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if(child != null){
                        val position = rv.getChildAdapterPosition(child)
                        val view = rv.layoutManager?.findViewByPosition(position)
                        view?.setBackgroundResource(R.drawable.rectangle_with_blackline)

                        //클릭한 날짜의 확정 약속들 조회
                        viewModel.requestReceivePlanResponse(viewModel.receiveInvitationDateList.value!![position].id)

                        for(i in 0..rv.adapter!!.itemCount){
                            val otherView = rv.layoutManager?.findViewByPosition(i)
                            if(otherView != view){
                                otherView?.setBackgroundResource(R.color.white)
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

    // 어댑터
    private fun setCheckBoxAdapter() {
        val checkboxListAdapter = ReceiveCheckListAdapter( onClickCheckbox = { viewModel.setIsClicked(it)} )

        binding.rvReceiveCheckbox.adapter = checkboxListAdapter
    }

    private fun setClickedAdapter() {
        val clickedAdapter = ReceiveSchduleListAdapter()

        binding.rvReceiveSchdule.adapter = clickedAdapter
    }

    // 옵저버
    private fun setListObserver() {

        viewModel.receiveInvitationData.observe(this) { receiveInvitationData ->
            //응답했던 초대장인지에 따라 뷰 결정
            isResponse = receiveInvitationData.isResponse

            if(isResponse){
                binding.clRecieveBottom.visibility = View.GONE
                binding.rvReceiveCheckbox.isEnabled = false
                binding.rvReceiveCheckbox.isClickable = false

            } else {
                setSingleChoice()
            }

            viewModel.setReceiveInvitationDate()

            // 함께 초대장 받은 사람 chip 그리기
            receiveInvitationData.newGuests.forEach {
                binding.cgRecieve.addView(Chip(this).apply {
                    text = it.username

                    setChipBackgroundColorResource(R.color.white)
                    setTextAppearance(R.style.chipTextPinkStyle)
                    chipStrokeWidth = 1.0F
                    setChipStrokeColorResource(R.color.pink01)
                    isCheckable = false
                    isEnabled = false

                })
            }

        }

        viewModel.receiveInvitationDateList.observe(this) { checkboxList ->
            with(binding.rvReceiveCheckbox.adapter as ReceiveCheckListAdapter){
                setIsResponse(isResponse)
                setCheckBox(checkboxList)
            }
        }

        viewModel.receivePlanResponseList.observe(this) {
            clickedList -> with(binding.rvReceiveSchdule.adapter as ReceiveSchduleListAdapter){
                setSchedule(clickedList)
                if(clickedList.isEmpty()) {
                    Log.d("************PLAN_RESPONSE", "EMPTY")
                    binding.tvReceiveSMsg.text = "약속이 없어요."
                    binding.tvReceiveSMsg.visibility = View.VISIBLE
                    binding.tvReceiveClickDay.visibility = View.GONE
                }else {
                    Log.d("************PLAN_RESPONSE", "NOT EMPTY")
                    binding.tvReceiveClickDay.text = clickedList[0].date
                    binding.tvReceiveSMsg.visibility = View.GONE
                    binding.tvReceiveClickDay.visibility = View.VISIBLE
                }
             }
        }

        //checkbox에 check된 가짓수가 1개 이상일 경우 버튼 활성화
        viewModel.isClicked.observe(this) {
            if(it > 0) {
                binding.btnReceiveYes.isClickable = true
                binding.btnReceiveYes.isEnabled = true
                binding.btnReceiveYes.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle_pink01_10, null)
            }
            else {
                binding.btnReceiveYes.isClickable = false
                binding.btnReceiveYes.isEnabled = false
                binding.btnReceiveYes.background = ResourcesCompat.getDrawable(resources, R.drawable.rectangle_gray02_10, null)
            }
        }

        viewModel.fetchState.observe(this){
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
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            
            binding.tvReceiveSMsg.text = "통신 실패로 정보를 받아오거나 보낼 수 없어요."
            binding.rvReceiveSchdule.visibility = View.GONE

        }

    }

    private fun initButtonClick(){

        //맨 아래 취소 버튼
        binding.btnReceiveNo.setOnClickListener {
            val dialogView = ReceiveNoDialogFragment()
            val bundle = Bundle()

            dialogView.arguments = bundle

            dialogView.setButtonClickListener( object :  ReceiveNoDialogFragment.OnButtonClickListener {
                override fun onCancelNoClicked() {

                }

                override fun onCancelYesClicked() {
                    if(invitationId != -1) {
                        viewModel.requestReceiveNoInvitation(invitationId)
                        finish()
                    }
                }
            })
            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }

        //맨 아래 수락 버튼
        binding.btnReceiveYes.setOnClickListener {
            val dialogView = ReceiveYesDialogFragment()
            val bundle = Bundle()
            val cblist = (binding.rvReceiveCheckbox.adapter as ReceiveCheckListAdapter).getCheckBoxList()

            val dateIdList : List<Int> = List(cblist.filter{ it.isSelected}.size) { i -> cblist.filter{ it.isSelected}[i].id }

            bundle.putParcelableArrayList("cblist", cblist as ArrayList<out Parcelable>)
            dialogView.arguments = bundle

            dialogView.setButtonClickListener( object : ReceiveYesDialogFragment.OnButtonClickListener {
                override fun onSendClicked() {
                    viewModel.requestReceiveYesInvitation(invitationId, dateIdList)
                    finish()
                }
                override fun onCancelClicked() {

                }
            })

            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }

        binding.ivReceiveBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ReceiveActivity::class.java)
            context.startActivity(intent)
        }
    }
}