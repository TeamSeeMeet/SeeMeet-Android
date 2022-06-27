package org.seemeet.seemeet.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.data.model.response.invitation.ConfirmedAndCanceld
import org.seemeet.seemeet.databinding.FragmentNotiDoneBinding
import org.seemeet.seemeet.ui.detail.DetailDialogFragment
import org.seemeet.seemeet.ui.notification.adapter.NotiDoneListAdapter
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.NotiViewModel
import retrofit2.HttpException

class NotiDoneFragment : Fragment() {
    private var _binding: FragmentNotiDoneBinding? = null
    val binding get() = _binding!!

    private val viewmodel: NotiViewModel by activityViewModels()
    private var doneListAdapter = NotiDoneListAdapter()
    private lateinit var donelist: List<ConfirmedAndCanceld>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiDoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SeeMeetSharedPreference.getLogin()) {
            viewmodel.requestAllInvitationList()
            setDoneAdapter()
            setDoneObserve()
        } else {
            setVisibility(binding.clNotiDoneNull, View.VISIBLE)
            binding.tvDoneNum.text = "0"
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.requestAllInvitationList()
        setDoneObserve()
    }

    private fun setDoneAdapter() {
        binding.rvDoneList.adapter = doneListAdapter
        binding.rvDoneList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.tvDoneNum.text = "${doneListAdapter.itemCount}"
    }

    private fun setDoneObserve() {
        viewmodel.invitationList.observe(viewLifecycleOwner) {
            viewmodel.setInviDoneList()
        }

        viewmodel.inviDoneList.observe(viewLifecycleOwner) { doneList ->
            with(binding.rvDoneList.adapter as NotiDoneListAdapter) {
                setDone(doneList)
                binding.tvDoneNum.text = doneList.size.toString()
                setVisibility(binding.ivDoneNetwork, View.GONE)
                if (doneList.isEmpty()) {
                    binding.clNotiDoneNull.visibility = View.VISIBLE
                } else {
                    binding.clNotiDoneNull.visibility = View.GONE
                }
                donelist = doneList
                initItemClickListener()
            }
        }

        viewmodel.fetchState.observe(viewLifecycleOwner){
            var message = ""
            when(it.second){
                BaseViewModel.FetchState.BAD_INTERNET-> {
                    message = "소켓 오류 / 서버와 연결에 실패하였습니다."
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val code = (it.first as HttpException).code()
                    message = "$code ERROR : \n ${it.first.message}"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    setVisibility(binding.clNotiDoneNull, View.GONE)
                    setVisibility(binding.ivDoneNetwork, View.VISIBLE)
                }
                else ->  {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }
            }

            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())

            if(message != ""){
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                binding.clNotiDoneNull.visibility = View.VISIBLE
            }
        }
    }

    private fun initItemClickListener(){
        doneListAdapter.setOnItemClickListener{ _, pos ->
            val id = donelist[pos].id
            showDialog(id)
        }
    }

    private fun showDialog(id:Int){
        var dialogView = DetailDialogFragment()
        val bundle = Bundle()

        dialogView.arguments = bundle
        dialogView.setButtonClickListener( object :  DetailDialogFragment.OnButtonClickListener {
            override fun onCancelNoClicked() {
            }

            override fun onCancelYesClicked() {
                viewmodel.deleteInvitation(id)
                Toast.makeText(context, "약속이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                viewmodel.requestAllInvitationList()
                setDoneObserve()
            }
        })
        dialogView.show(childFragmentManager, "send wish checkbox time")
    }

    private fun setVisibility(view: View, visibility: Int){
        view.visibility = visibility
    }
}

