package org.seemeet.seemeet.ui.apply

import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentSecondApplyBinding
import org.seemeet.seemeet.ui.apply.timepicker.setTimeInterval
import org.seemeet.seemeet.ui.receive.ReceiveNoDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class SecondApplyFragment : Fragment() {

    private var _binding: FragmentSecondApplyBinding? = null
    val binding get() = _binding!!
    var timeString = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondApplyBinding.inflate(layoutInflater)
/*
        binding.time.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                timeString = "${hourOfDay}시 ${minute}분"
                binding.time.text = "시간 : " + timeString
            }
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }*/
/*
        binding.time.text =
            Calendar.getInstance().get(Calendar.HOUR).toString() + Calendar.getInstance()
                .get(Calendar.MINUTE).toString()
*/

       // binding.time.setText(defaultTime())
        binding.time.setOnClickListener {
            getTime(binding.time, requireContext(), binding.time.text.toString())

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                timeString = "${hourOfDay}시 ${minute}분"
                binding.time.text = "시간 : " + timeString
            }
        }

        return binding.root
    }
/*
    fun defaultTime(): String {
        val cal = Calendar.getInstance()
        if(Calendar.MINUTE>=0 && Calendar.MINUTE<30){
            //시간을 1 더하고 분은 00
            //11시부터 11시 29까지 -> 12시 00분
                cal.set(Calendar.HOUR_OF_DAY,Calendar.HOUR+1)
            cal.set(Calendar.MINUTE, 0)
            return SimpleDateFormat("aa hh:mm").format(cal.time)
        }
        if(Calendar.MINUTE>=30 && Calendar.MINUTE<=59){
            //시간을 1 더하고 분은 30
            //11시 30분부터 11시 59분까지 -> 12시 30분
            cal.set(Calendar.HOUR_OF_DAY,Calendar.HOUR+1)
            cal.set(Calendar.MINUTE, 30)
            return SimpleDateFormat("aa hh:mm").format(cal.time)
        }
        return SimpleDateFormat("aa hh:mm").format(cal.time)

    }*/

    fun getTime(textView: TextView, context: Context, string: String) {

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("aa hh:mm").format(cal.time)
        }

        TimePickerDialog(context, timeSetListener, Calendar.HOUR, Calendar.MINUTE, false)
    }
}