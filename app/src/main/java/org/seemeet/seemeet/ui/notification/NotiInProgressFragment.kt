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
            setNotiNullVisibility(View.VISIBLE)
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
                if (inProgressList.isEmpty()) {
                    setNotiNullVisibility(View.VISIBLE)
                } else {
                    setNotiNullVisibility(View.GONE)
                }
            }
        }

        viewmodel.fetchState.observe(viewLifecycleOwner){
            var message = ""
            when( it.second){
                BaseViewModel.FetchState.BAD_INTERNET-> {
                    message = "소켓 오류 / 서버와 연결에 실패하였습니다."
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val code = (it.first as HttpException).code()
                    message = "$code ERROR : \n ${it.first.message}"
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    message = "호스트를 확인할 수 없습니다. 네트워크 연결을 확인해주세요"
                }
                else ->  {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                }
            }

            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            binding.clNotiInProgressNull.visibility = View.VISIBLE
        }
    }

    private fun setNotiNullVisibility(notificationNull: Int) {
        binding.clNotiInProgressNull.visibility = notificationNull
    }

    override fun onResume() {
        super.onResume()
        if (SeeMeetSharedPreference.getLogin()) {
            viewmodel.requestAllInvitationList()
        } else {
            setNotiNullVisibility(View.VISIBLE)
            binding.tvInProgressNum.text = "0"
        }
    }
}