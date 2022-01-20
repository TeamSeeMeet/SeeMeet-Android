package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding
import org.seemeet.seemeet.ui.apply.adapter.ApplyFriendAdapter

class FirstApplyFragment : Fragment() {

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
        initScrollEvent()

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

            if (!binding.etToWho.text.isNullOrBlank()) {
                binding.etToWho.text.clear()
            }
            binding.tvWho.visibility = View.VISIBLE
            binding.etToWho.clearFocus()
            binding.rvFriend.visibility = View.INVISIBLE
            false
        }

        //아이템을 클릭했을 때
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
                        if (isNullorBlank()) { //셋 중 하나라도 작성 안 됐을 때
                            unactiveBtn()
                        } else { //셋 다 작성했을 때
                            activeBtn()
                        }
                        adapter.addItem(
                            ApplyFriendData(
                                R.drawable.ic_btn_remove,
                                this.text.toString()
                            )
                        )
                        adapter.sortItem(
                            ApplyFriendData(
                                R.drawable.ic_btn_remove,
                                this.text.toString()
                            )
                        )
                        //TODO: 아이템 순서대로 들어가게 해야함(이름 순) -> 지금은 그냥 뒤로 차례로 들어감
                    }
                })
            if (isNullorBlank()) { //셋 중 하나라도 작성 안 됐을 때
                unactiveBtn()
            } else { //셋 다 작성했을 때
                activeBtn()
            }
            adapter.removeItem(adapter.getPosition())
        }
    }

    fun initFocusBackground() {
        binding.etToWho.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.rvFriend.visibility = View.VISIBLE
                binding.applyAppointment.visibility = View.INVISIBLE
                binding.applyContent.visibility = View.INVISIBLE
                binding.applyWrite.visibility = View.INVISIBLE
                binding.clContent.visibility = View.INVISIBLE
            } else {
                binding.rvFriend.visibility = View.INVISIBLE
                binding.applyAppointment.visibility = View.VISIBLE
                binding.applyContent.visibility = View.VISIBLE
                binding.applyWrite.visibility = View.VISIBLE
                binding.clContent.visibility = View.VISIBLE
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
            adapter.setSearchWord(binding.etToWho.text.toString())

            if (binding.etToWho.text.isNullOrBlank()) {
                binding.tvWho.visibility = View.VISIBLE
            } else binding.tvWho.visibility = View.INVISIBLE
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

    //여기서 하나라도 성립하면 true 반환
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

    private fun initScrollEvent() {
        binding.etDetail.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                binding.svFirstApply.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })
    }

/*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.rvFriend.visibility = View.INVISIBLE

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