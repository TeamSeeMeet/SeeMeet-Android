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
import org.seemeet.seemeet.databinding.FragmentNotiDoneBinding
import org.seemeet.seemeet.ui.notification.adapter.NotiDoneListAdapter
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.ui.viewmodel.NotiViewModel
import retrofit2.HttpException

class NotiDoneFragment : Fragment() {
    private var _binding: FragmentNotiDoneBinding? = null
    val binding get() = _binding!!

    private val viewmodel: NotiViewModel by activityViewModels()

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
            binding.clNotiDoneNull.visibility = View.VISIBLE
            binding.tvDoneNum.text = "0"
        }
    }

    private fun setDoneAdapter() {
        val doneListAdapter = NotiDoneListAdapter()
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

                if (doneList.isEmpty()) {
                    binding.clNotiDoneNull.visibility = View.VISIBLE
                } else {
                    binding.clNotiDoneNull.visibility = View.GONE
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
            binding.clNotiDoneNull.visibility = View.VISIBLE
        }
    }
}

