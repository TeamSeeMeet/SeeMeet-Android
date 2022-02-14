package org.seemeet.seemeet.ui.receive

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.invitation.ReceiveInvitationDate
import org.seemeet.seemeet.databinding.DialogReceiveYesDiagloBinding
import org.seemeet.seemeet.util.TimeParsing

class ReceiveYesDiagloFragment : DialogFragment() {

    private var _binding : DialogReceiveYesDiagloBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogReceiveYesDiagloBinding.inflate(
            inflater, container, false)

        val cbList : ArrayList<ReceiveInvitationDate> = arguments?.getParcelableArrayList<Parcelable>("cblist") as ArrayList<ReceiveInvitationDate>
        initCheckedTimeList(cbList)

        return binding.root
    }

    private fun initCheckedTimeList(cbList : ArrayList<ReceiveInvitationDate>){
        var cnt = 0

        cbList.forEach{
            if(it.isSelected){
                val time = it.start.TimeParsing() + " ~" + it.end.TimeParsing()
                when(cnt){
                    0 -> {
                        binding.tvReceiDateWish1.text = it.date
                        binding.tvReceiTimeWish1.text = time
                        cnt++
                    }
                    1 -> {
                        binding.tvReceiDateWish2.text = it.date
                        binding.tvReceiTimeWish2.text = time
                        cnt++
                    }
                    2 -> {
                        binding.tvReceiDateWish3.text = it.date
                        binding.tvReceiTimeWish3.text = time
                        cnt++
                    }
                    3 -> {
                        binding.tvReceiDateWish4.text = it.date
                        binding.tvReceiTimeWish4.text  = time
                        cnt++
                    }
                }
            }
        }
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
        dialog.window!!.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.rectangle_white_10, null))
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