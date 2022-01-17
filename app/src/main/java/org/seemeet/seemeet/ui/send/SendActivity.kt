package org.seemeet.seemeet.ui.send

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.InviData
import org.seemeet.seemeet.data.local.ReminderData
import org.seemeet.seemeet.databinding.ActivitySendBinding
import org.seemeet.seemeet.ui.detail.DetailActivity
import org.seemeet.seemeet.ui.main.adapter.ReminderListAdapter
import org.seemeet.seemeet.ui.send.adapter.SendInvitationAdapter
import org.seemeet.seemeet.ui.send.adapter.SendItemDetailsLookup
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

                        // 지금은 일괄적으로 다 같은 데이터 넣고 있는데, 나중에 위의 체크박스에서 포지션 가지고 id 값 불러와서 서버에 해당 데이터 요청하기...
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


}