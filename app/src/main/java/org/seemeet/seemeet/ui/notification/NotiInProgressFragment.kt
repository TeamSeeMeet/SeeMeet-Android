package org.seemeet.seemeet.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.seemeet.seemeet.databinding.FragmentNotiInProgressBinding
import org.seemeet.seemeet.ui.notification.adapter.NotiIngListAdapter
import org.seemeet.seemeet.ui.viewmodel.NotiIngViewModel

class NotiInProgressFragment : Fragment() {
    private var _binding : FragmentNotiInProgressBinding? = null
    val binding get() = _binding!!

    private val viewmodel : NotiIngViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiInProgressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()

        // (1) 더미데이터 셋팅 _ 이후 서버통신 시 교체
        viewmodel.setIngList()

        // (2) 어뎁터와 옵저버 셋팅
        setIngAdapter()
        setIngObserve()
    }

    private fun initClickListener(){

    }

    // 어댑터
    private fun setIngAdapter() {
        val ingListAdapter = NotiIngListAdapter()

        binding.rvIngList.adapter = ingListAdapter
    }

    // 옵저버 _ 위에서 (1)로 데이터 넣을 경우 옵저버가 관찰하다가 업데이트함.
    private fun setIngObserve() {
        viewmodel.ingList.observe(viewLifecycleOwner){
            ingList -> with(binding.rvIngList.adapter as NotiIngListAdapter){
            setIng(ingList)
            }
        }
    }
}