package org.seemeet.seemeet.ui.receive

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.CheckboxData
import org.seemeet.seemeet.databinding.DialogReceiveYesDiagloBinding

class ReceiveYesDiagloFragment : DialogFragment() {

    private var _binding : DialogReceiveYesDiagloBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DialogReceiveYesDiagloBinding.inflate(
            inflater, container, false)


        val bundle = arguments
        val cblist : ArrayList<CheckboxData> = bundle?.getParcelableArrayList<Parcelable>("cblist") as ArrayList<CheckboxData>
        var cnt : Int = 0
        cblist.forEach{
            if(it.flag){
                when(cnt){
                    0 -> {
                        binding.tvReceiDateWish1.text = it.date
                        binding.tvReceiTimeWish1.text = it.time
                        cnt++
                    }
                    1 -> {
                        binding.tvReceiDateWish2.text = it.date
                        binding.tvReceiTimeWish2.text = it.time
                        cnt++
                    }
                    2 -> {
                        binding.tvReceiDateWish3.text = it.date
                        binding.tvReceiTimeWish3.text = it.time
                        cnt++
                    }
                    3 -> {
                        binding.tvReceiDateWish4.text = it.date
                        binding.tvReceiTimeWish4.text = it.time
                        cnt++
                    }

                }
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReceiDiaSend.setOnClickListener {
            buttonClickListener.onSendClicked()
            dismiss()
        }
        binding.btnReceiDiaCancel.setOnClickListener {
            buttonClickListener.onCancelClicked()
            dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_white_top10))

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 인터페이스
    interface OnButtonClickListener {
        fun onCancelClicked()
        fun onSendClicked()
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }


}