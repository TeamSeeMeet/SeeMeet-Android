package org.seemeet.seemeet.ui.apply

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.seemeet.seemeet.databinding.FragmentSecondApplyBinding
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

        binding.time.text =
            Calendar.getInstance().get(Calendar.HOUR).toString() + Calendar.getInstance()
                .get(Calendar.MINUTE).toString()


        binding.time.setOnClickListener {
            getTime(binding.time, requireContext(), binding.time.text.toString())
        }



        return binding.root
    }

    fun getTime(textView: TextView, context: Context, string: String){

        val cal = Calendar.getInstance()

        String

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("aa hh:mm").format(cal.time)
        }

        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false).show()

    }

}