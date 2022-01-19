package org.seemeet.seemeet.ui.apply

import android.app.ActionBar
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.seemeet.seemeet.databinding.ActivityApplyBinding
import java.text.SimpleDateFormat
import java.util.*

class ApplyActivity : AppCompatActivity() {
    private val binding: ActivityApplyBinding by lazy {
        ActivityApplyBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvTimepicker.text = defaultTime(0) //처음 딱 들어갔을 때는 디폴트 타임으로 띄우기
        binding.tvStartTimepicker.text = defaultTime(1)
        initClickListener()
    }

    private fun initClickListener() {
        binding.ivX.setOnClickListener {
            finish()
        }
        binding.tvTimepicker.setOnClickListener {
           // Toast.makeText(this@ApplyActivity, "자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            //오전 09 : 01

            var ampm = binding.tvTimepicker.text.substring(0,2)
            var hour = binding.tvTimepicker.text.substring(3,5)
            var min = binding.tvTimepicker.text.substring(8,10)
            if(ampm.equals("오후")){
                if(!hour.equals("12")){
                    hour = (hour.toInt()+12).toString()
                }
            }else{
                if(hour.equals("12")){
                    hour = "0"
                }
            }
            Log.d("tests",ampm+"  "+hour+"  "+min)
            getTime(binding.tvTimepicker, this@ApplyActivity, hour, min,0)

        }

        binding.tvStartTimepicker.setOnClickListener{
            var ampm = binding.tvStartTimepicker.text.substring(0,2)
            var hour = binding.tvStartTimepicker.text.substring(3,5)
            var min = binding.tvStartTimepicker.text.substring(8,10)
            if(ampm.equals("오후")){
                if(!hour.equals("12")){
                    hour = (hour.toInt()+12).toString()
                }
            }else{
                if(hour.equals("12")){
                    hour = "0"
                }
            }
            Log.d("tests",ampm+"  "+hour+"  "+min)
            getTime(binding.tvStartTimepicker, this@ApplyActivity, hour, min,1)

        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ApplyActivity::class.java)
            context.startActivity(intent)
        }
    }

    //현재 시간 띄우면 되는데
    fun defaultTime(i:Int): String {
        val cal = Calendar.getInstance() //
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        //현재 시간이 cal에 있음

        if(i==0){
            if(minutes>=0 && minutes<30){
                //시간을 1 더하고 분은 00
                //11시부터 11시 29까지 -> 12시 00분
                cal.set(Calendar.HOUR_OF_DAY,hour+1)
                cal.set(Calendar.MINUTE, 0)
            }else{
                //시간을 1 더하고 분은 30
                //11시 30분부터 11시 59분까지 -> 12시 30분
                cal.set(Calendar.HOUR_OF_DAY,hour+1)
                cal.set(Calendar.MINUTE, 30)
            }
        }
        else if(i==1){
            if(minutes>=0 && minutes<30){
                //시간을 1 더하고 분은 00
                //11시부터 11시 29까지 -> 12시 00분
                cal.set(Calendar.HOUR_OF_DAY,hour+2)
                cal.set(Calendar.MINUTE, 0)
            }else{
                //시간을 1 더하고 분은 30
                //11시 30분부터 11시 59분까지 -> 12시 30분
                cal.set(Calendar.HOUR_OF_DAY,hour+2)
                cal.set(Calendar.MINUTE, 30)
            }
        }
        return SimpleDateFormat("aa hh : mm").format(cal.time)

    }


    fun getTime(textView: TextView, context: Context,hour: String, min: String,i: Int) {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("aa hh : mm").format(cal.time) //타임피커에서 선택한 시간 받아서 텍스트에 넣어줌
        }

        val tp = TimePickerDialog(context, timeSetListener, hour.toInt(), min.toInt(), false)
        if(i==0){
            tp.window?.attributes?.x = 100
            tp.window?.attributes?.y = 98
        }
        else{

            tp.window?.attributes?.x = 100
            tp.window?.attributes?.y = 160
        }
        tp.show()//다이얼로그 띄울 때 시간 보내줌

    }
}