package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding
import org.seemeet.seemeet.ui.apply.adapter.ApplyFriendAdapter

class FirstApplyFragment : Fragment() {

    private lateinit var callback: OnBackPressedCallback

    private var _binding: FragmentFirstApplyBinding? = null
    val binding get() = _binding!!
    private lateinit var adapter: ApplyFriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstApplyBinding.inflate(layoutInflater)

        initClickListener()
        initAutoCompletetv()
        initFocusBackground()
        initTextChangedListener()

        return binding.root
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_firstApplyFragment_to_secondApplyFragment)
        }

        binding.ivTitleClear.setOnClickListener {
            binding.etTitle.setText(null)
            binding.ivTitleClear.visibility = View.INVISIBLE
        }
    }

    private fun initAutoCompletetv() {

        adapter = ApplyFriendAdapter()

        binding.rvFriend.adapter = adapter

        adapter.applyfriendList.addAll(
            listOf(
                ApplyFriendData(R.drawable.ic_btn_remove, "강유나"),
                ApplyFriendData(R.drawable.ic_btn_remove, "나지현"),
                ApplyFriendData(R.drawable.ic_btn_remove, "도승윤"),
                ApplyFriendData(R.drawable.ic_btn_remove, "박나은"),
                ApplyFriendData(R.drawable.ic_btn_remove, "박주혁"),
                ApplyFriendData(R.drawable.ic_btn_remove, "박예림"),
                ApplyFriendData(R.drawable.ic_btn_remove, "박한빈"),
                ApplyFriendData(R.drawable.ic_btn_remove, "박수현"),
                ApplyFriendData(R.drawable.ic_btn_remove, "김주현"),
                ApplyFriendData(R.drawable.ic_btn_remove, "최유현")
            )
        )
        adapter.notifyDataSetChanged()

        //키보드에서 완료 버튼 누르면
        binding.etToWho.setOnEditorActionListener { textView, i, keyEvent ->
            binding.tvWho.visibility = View.VISIBLE
            binding.etToWho.clearFocus()
            binding.rvFriend.visibility = View.INVISIBLE
            false
        }

        //아이템을 클릭했을 때 //TODO 리스트에서 삭제돼야함
        adapter.setOnItemClickListener {
            binding.chipGroup.addView(
                (layoutInflater.inflate(
                    R.layout.chip_layout,
                    null,
                    false
                ) as Chip).apply {
                    text = it //name 받아온 것
                    this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                    this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                    binding.etToWho.text.clear()
                    binding.etToWho.setPadding(binding.etToWho.paddingLeft + 250, 0, 0, 0)
                    setOnCloseIconClickListener {
                        binding.chipGroup.removeView(this)
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft - 250, 0, 0, 0)
                    }
                })
        }
    }

    fun initFocusBackground() {
        binding.etToWho.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.tvWho.visibility = View.INVISIBLE
                binding.rvFriend.visibility = View.VISIBLE
            } else {
                binding.rvFriend.visibility = View.INVISIBLE
            }
        }

        binding.etTitle.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10_stroke_pink)

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
        binding.etToWho.addTextChangedListener {
            if (isNullorBlank()) { //셋 중 하나라도 작성 안 됐을 때
                unactiveBtn()
            } else { //셋 다 작성했을 때
                activeBtn()
            }
            Log.d("test,", binding.chipGroup.childCount.toString())
            adapter.setSearchWord(binding.etToWho.text.toString())
        }

        binding.etTitle.addTextChangedListener {
            if (binding.etTitle.text.isNullOrBlank()) { //공백일 때
                binding.ivTitleClear.visibility = View.INVISIBLE
            } else {
                binding.ivTitleClear.visibility = View.VISIBLE
            }
            if (isNullorBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }
        binding.etDetail.addTextChangedListener {
            if (isNullorBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }
    }

    //여기서 하나라도 성립하면 true 반환 //TODO 칩 그룹 개수 버그 생김 수정 필요
    private fun isNullorBlank(): Boolean {
        return binding.chipGroup.childCount == 0 || binding.etTitle.text.isNullOrBlank() || binding.etDetail.text.isNullOrBlank() || (!binding.etToWho.text.isNullOrBlank()) //TODO 여기 chip 개수 !!!!
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

/*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.tvWho.visibility = View.INVISIBLE

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}