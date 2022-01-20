package org.seemeet.seemeet.ui.receive

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.InviData
import org.seemeet.seemeet.databinding.DialogHomeNoLoginBinding
import org.seemeet.seemeet.databinding.DialogReceiveNoDialogBinding

class DialogHomeNoLoginFragment : DialogFragment() {

    private var _binding : DialogHomeNoLoginBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DialogHomeNoLoginBinding.inflate(
            inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHomeNologinDialogLogin.setOnClickListener {
            buttonClickListener.onLoginClicked()
            dismiss()
        }
        binding.btnHomeNologinDialogCancel.setOnClickListener {
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
        fun onLoginClicked()
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }


}