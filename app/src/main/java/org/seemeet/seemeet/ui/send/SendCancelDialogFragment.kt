package org.seemeet.seemeet.ui.receive

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.DialogSendCancelBinding

class SendCancelDialogFragment : DialogFragment() {

    private var _binding : DialogSendCancelBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogSendCancelBinding.inflate(
            inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendDiaCancelYes.setOnClickListener {
            buttonClickListener.onCancelYesClicked()
            dismiss()
        }
        binding.btnSendDiaCancelNo.setOnClickListener {
            buttonClickListener.onCancelNoClicked()
            dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.rectangle_white_10)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 인터페이스
    interface OnButtonClickListener {
        fun onCancelNoClicked()
        fun onCancelYesClicked()
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }


}