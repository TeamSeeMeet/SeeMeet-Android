package org.seemeet.seemeet.ui.receive

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.invitation.SendInvitationDate
import org.seemeet.seemeet.databinding.DialogSendConfirmBinding
import org.seemeet.seemeet.util.TimeParsing


class SendConfirmDialogFragment : DialogFragment() {

    private var _binding : DialogSendConfirmBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogSendConfirmBinding.inflate(
            inflater, container, false)


        val bundle = arguments
        val choice = bundle?.getSerializable("choice") as SendInvitationDate
        val check = bundle.getInt("check")
        Log.d("*******sendConfirmDialog", choice.start + "," + check.toString())

        when(check){
            1 -> {
                binding.tvSendCheck1.visibility = View.VISIBLE
                binding.tvSendCheck2.visibility = View.GONE
            }
            2 -> {
                binding.tvSendCheck2.text = resources.getString(R.string.send_dialog_check2)
                binding.tvSendCheck1.visibility = View.GONE
                binding.tvSendCheck2.visibility = View.VISIBLE
            }
            3 -> {
                binding.tvSendCheck2.text = resources.getString(R.string.send_dialog_check3)
                binding.tvSendCheck1.visibility = View.GONE
                binding.tvSendCheck2.visibility = View.VISIBLE
            }
        }

        binding.tvSendDateConfirm.text = choice.date
        binding.tvSendTimeConfirm.text = String.format(resources.getString(R.string.time_start_end), choice.start.TimeParsing(), choice.end.TimeParsing())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendDiaConfirmYes.setOnClickListener {
            buttonClickListener.onConfirmYesClicked()
            dismiss()
        }
        binding.btnSendDiaConfirmNo.setOnClickListener {
            buttonClickListener.onConfirmNoClicked()
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
        fun onConfirmNoClicked()
        fun onConfirmYesClicked()
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }


}