package org.seemeet.seemeet.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.seemeet.seemeet.data.local.NotificationDoneData
import org.seemeet.seemeet.databinding.FragmentNotiDoneBinding
import org.seemeet.seemeet.ui.notification.adapter.NotiDoneListAdapter
import org.seemeet.seemeet.ui.viewmodel.NotiDoneViewModel

class NotiDoneFragment : Fragment() {
    private var _binding : FragmentNotiDoneBinding? = null
    val binding get() = _binding!!

    private val viewmodel : NotiDoneViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiDoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // (1) 더미데이터 셋팅 _ 이후 서버통신 시 교체
        viewmodel.setDoneList()

        // (2) 어뎁터와 옵저버 셋팅
        setDoneAdapter()
        setDoneObserve()
    }

    // 어댑터
    private fun setDoneAdapter() {
        val doneListAdapter = NotiDoneListAdapter(
        )
        binding.rvDoneList.adapter = doneListAdapter
        binding.rvDoneList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        binding.tvDoneNum.text = "${doneListAdapter.itemCount}"
    }

    // 옵저버 _ 위에서 (1)로 데이터 넣을 경우 옵저버가 관찰하다가 업데이트함.
    private fun setDoneObserve() {
        viewmodel.doneList.observe(viewLifecycleOwner){
                doneList-> with(binding.rvDoneList.adapter as NotiDoneListAdapter){
            setDone(doneList as MutableList<NotificationDoneData>)
            binding.tvDoneNum.text = "${doneList.size}"

            if(doneList.isEmpty()) {
                binding.clNotiDoneNull.visibility = View.VISIBLE
            }else {
                binding.clNotiDoneNull.visibility = View.GONE
            }
                }
        }
    }
}

