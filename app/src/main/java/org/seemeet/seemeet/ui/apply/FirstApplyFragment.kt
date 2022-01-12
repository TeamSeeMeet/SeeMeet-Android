package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

        initFocusBackground()
        initTextChangedListener()

        return binding.root
    }

    fun initFocusBackground(){
        binding.etId.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10_stroke_pink)
                binding.etId.setHint(null)

            } else {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10)
            }
        }

        binding.etDetail.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10_stroke_pink)
            } else {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10)
            }
        }
    }

    fun initTextChangedListener() {
        binding.etToWho.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isNullOrBlank()) {
                    unactiveBtn()
                } else {
                    activeBtn()
                }
            }
        })

        binding.etId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isNullOrBlank()) {
                    unactiveBtn()
                } else {
                    activeBtn()
                }
            }
        })

        binding.etDetail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isNullOrBlank()) {
                    unactiveBtn()
                } else {
                    activeBtn()
                }
            }
        })

    }

    private fun isNullOrBlank(): Boolean {
        return binding.etDetail.text.isNullOrBlank() || binding.etId.text.isNullOrBlank() || binding.etToWho.text.isNullOrBlank()
    }

    private fun activeBtn() {
        binding.btnNext.setBackgroundResource(R.drawable.rectangle_pink_10)
        binding.btnNext.isClickable = true // 버튼 클릭할수 있게
        binding.btnNext.isEnabled = true // 버튼 활성화
    }

    private fun unactiveBtn() {
        binding.btnNext.setBackgroundResource(R.drawable.rectangle_gray_radius_10)
        binding.btnNext.isClickable = false // 버튼 클릭할수 없게
        binding.btnNext.isEnabled = false // 버튼 비활성화
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}