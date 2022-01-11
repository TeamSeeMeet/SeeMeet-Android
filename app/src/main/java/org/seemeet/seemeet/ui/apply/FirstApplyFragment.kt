package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding


class FirstApplyFragment : Fragment() {

    private var _binding: FragmentFirstApplyBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstApplyBinding.inflate(layoutInflater)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_firstApplyFragment_to_secondApplyFragment)
        }
        return binding.root

        // temp()
    }
/*
    fun temp(){
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            //텍스트를 입력 후
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //텍스트 입력 전
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 중
                if(binding.etTitle.length() < 4) {
                    // 패스워드의 길이가 4미만이면
                    binding.btnNext.isClickable = false // 버튼 클릭할수 없게
                    binding.btnNext.isEnabled = false // 버튼 비활성화
                }
                else {
                    binding.btnNext.isClickable = true // 버튼 클릭할수 있게
                    binding.btnNext.isEnabled = true // 버튼 활성화
                 }
             }
        })
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}