package org.seemeet.seemeet.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.FragmentNotiInProgressBinding
import org.seemeet.seemeet.ui.notification.adapter.NotiInProgressListAdapter
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.NotiViewModel
import retrofit2.HttpException

class NotiInProgressFragment : Fragment() {
    private var _binding: FragmentNotiInProgressBinding? = null
    val binding get() = _binding!!

    private val viewmodel: NotiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiInProgressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SeeMeetSharedPreference.getLogin()) {
            viewmodel.requestAllInvitationList()
            setInProgressAdapter()
            setInProgressObserve()
        } else {
            setVisibility(binding.clNotiInProgressNull, View.VISIBLE)
            binding.tvInProgressNum.text = "0"
        }
    }

    private fun setInProgressAdapter() {
        val inProgressListAdapter = NotiInProgressListAdapter()
        binding.rvInProgressList.adapter = inProgressListAdapter
        binding.tvInProgressNum.text = "${inProgressListAdapter.itemCount}"
    }

    private fun setInProgressObserve() {
        viewmodel.invitationList.observe(viewLifecycleOwner) {
            viewmodel.setInviInProgressList()
        }

        viewmodel.inviInProgressList.observe(viewLifecycleOwner) { inProgressList ->
            with(binding.rvInProgressList.adapter as NotiInProgressListAdapter) {
                setInProgressList(inProgressList)
                binding.tvInProgressNum.text = inProgressList.size.toString()
                setVisibility(binding.ivInProgressNetwork, View.GONE)
                if (inProgressList.isEmpty()) {
                    setVisibility(binding.clNotiInProgressNull, View.VISIBLE)
                } else {
                    setVisibility(binding.clNotiInProgressNull, View.GONE)
                }
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
                    setVisibility(binding.clNotiInProgressNull, View.GONE)
                    setVisibility(binding.ivInProgressNetwork, View.VISIBLE)
                }
                else ->  {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }
            }

            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())

            if(message != ""){
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setVisibility(view: View, visibility: Int){
        view.visibility = visibility
    }

    override fun onResume() {
        super.onResume()
        if (SeeMeetSharedPreference.getLogin()) {
            viewmodel.requestAllInvitationList()
        } else {
            setVisibility(binding.clNotiInProgressNull, View.VISIBLE)
            binding.tvInProgressNum.text = "0"
        }
    }
}