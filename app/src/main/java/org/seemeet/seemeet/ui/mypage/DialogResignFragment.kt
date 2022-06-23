package org.seemeet.seemeet.ui.mypage

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentDialogResignBinding

class DialogResignFragment : DialogFragment() {

    private var _binding: FragmentDialogResignBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogResignBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnResignNo.setOnClickListener {
            buttonClickListener.onResignNoClicked()
            dismiss()
        }
        binding.btnResignYes.setOnClickListener {
            buttonClickListener.onResignYesClicked()
            dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.rectangle_white_10,
                null
            )
        )

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 인터페이스
    interface OnButtonClickListener {
        fun onResignNoClicked()
        fun onResignYesClicked()
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }


}