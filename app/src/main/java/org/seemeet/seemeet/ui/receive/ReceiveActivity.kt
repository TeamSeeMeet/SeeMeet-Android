package org.seemeet.seemeet.ui.receive

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ReminderData
import org.seemeet.seemeet.databinding.ActivityReceiveBinding
import org.seemeet.seemeet.ui.MainActivity
import org.seemeet.seemeet.ui.detail.DetailActivity
import org.seemeet.seemeet.ui.main.adapter.ReminderListAdapter
import org.seemeet.seemeet.ui.receive.adapter.ReceiveCheckListAdapter
import org.seemeet.seemeet.ui.receive.adapter.ReceiveSchduleListAdapter
import org.seemeet.seemeet.ui.viewmodel.ReceiveViewModel
import android.os.Parcelable




class ReceiveActivity : AppCompatActivity() {

    private lateinit var binding:  ActivityReceiveBinding
    private val viewModel: ReceiveViewModel by viewModels() //위임초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receive)
        binding.viewModel = viewModel
        viewModel.setReceiveData()

        setSingleChoice()

        setCheckBoxAdapter()
        setClickedAdapter()

        setListObserver()
        setTextColorSpan()

        initButtonClick()

    }

    private fun setTextColorSpan(){
        val str = resources.getString(R.string.recieve_choice_msg)
        val word = "날짜를 선택"
        val start = str.indexOf(word)
        val end = start + word.length
        val ss = SpannableStringBuilder(str)

        ss.setSpan(ForegroundColorSpan(Color.parseColor("#FA555C")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(RelativeSizeSpan(1.2f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvRecieveCheckboxMsg.text = ss
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
                        Log.d("*******************tag", viewModel.checkboxList.value!![position].time)

                        // 지금은 일괄적으로 다 같은 데이터 넣고 있는데, 나중에 위의 체크박스에서 포지션 가지고 id 값 불러와서 서버에 해당 데이터 요청하기...
                        viewModel.setSheduleListData()
                        binding.tvReceiveSMsg.visibility = View.GONE

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
                TODO("Not yet implemented")
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                TODO("Not yet implemented")
            }
        })
    }

    // 어댑터
    private fun setCheckBoxAdapter() {
        val checkboxListAdapter = ReceiveCheckListAdapter( onClickCheckbox = {viewModel.setIsClicked(it)} )

        binding.rvReceiveCheckbox.adapter = checkboxListAdapter
    }

    private fun setClickedAdapter() {
        val clickedAdapter = ReceiveSchduleListAdapter()

        binding.rvReceiveSchdule.adapter = clickedAdapter
    }

    // 옵저버
    private fun setListObserver() {
        viewModel.checkboxList.observe(this, Observer {
            checkboxList ->
            with(binding.rvReceiveCheckbox.adapter as ReceiveCheckListAdapter){
                setCheckBox(checkboxList)
            }
        })
        viewModel.recieverList.observe(this, Observer {
            receiverList ->
            receiverList.forEach {
                binding.cgRecieve.addView(Chip(this).apply{
                    text = it.name
                    if(it.response){
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
                Log.d("**********************받은이", it.name)
            }

        })

        viewModel.clickedList.observe(this, Observer {
            clickedList -> with(binding.rvReceiveSchdule.adapter as ReceiveSchduleListAdapter){
                setSchedule(clickedList)
                if(clickedList.isEmpty()) {
                    binding.tvReceiveSMsg.text = "약속이 없어요."
                    binding.tvReceiveSMsg.visibility = View.VISIBLE
                    binding.tvReceiveClickDay.visibility = View.GONE
                }else {
                    binding.tvReceiveClickDay.text = clickedList[0].date
                    binding.tvReceiveSMsg.visibility = View.GONE
                }
             }
        })

        viewModel.isClicked.observe(this, Observer {
            it ->
            Log.d("***************isClicked", it.toString())
            if(it > 0) {
                binding.btnReceiveYes.isClickable = true
                binding.btnReceiveYes.isEnabled = true
                binding.btnReceiveYes.background = resources.getDrawable(R.drawable.rectangle_pink01_10)
            }
            else {
                binding.btnReceiveYes.isClickable = false
                binding.btnReceiveYes.isEnabled = false
                binding.btnReceiveYes.background = resources.getDrawable(R.drawable.rectangle_gray02_10)
            }
        })

    }

    private fun initButtonClick(){

        //맨 아래 수락 버튼
        binding.btnReceiveYes.setOnClickListener {
            var dialogView = ReceiveYesDiagloFragment()
            val bundle = Bundle()
            val cblist = (binding.rvReceiveCheckbox.adapter as ReceiveCheckListAdapter).getCheckBoxList()

            bundle.putParcelableArrayList("cblist", cblist as ArrayList<out Parcelable>)
            dialogView.arguments = bundle


            dialogView.setButtonClickListener( object : ReceiveYesDiagloFragment.OnButtonClickListener {
                override fun onSendClicked() {
                    //여기서 데이터 전송.
                    //위의 cblist에서 flag가 true인 애들 아이디만 골라서 전송해주기.
                }

                override fun onCancelClicked() {

                }
            })

            dialogView.show(supportFragmentManager, "send wish checkbox time")
        }
    }


}