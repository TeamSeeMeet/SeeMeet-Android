package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding
import java.util.*

class FirstApplyFragment : Fragment() {

    private var _binding: FragmentFirstApplyBinding? = null
    val binding get() = _binding!!
    private var friendlist // 데이터를 넣은 리스트변수
            : MutableList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstApplyBinding.inflate(layoutInflater)

        initClickListener()
        initAutiComletetv()
        initFocusBackground()
        initTextChangedListener()

        return binding.root
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_firstApplyFragment_to_secondApplyFragment)
        }

        binding.autoEtToWho.setOnClickListener { //todo 사실 여기서 autoEtToWho를 클릭했을 때가 아니라 아이템을 눌렀을 경우여야함
            val string = binding.autoEtToWho.text
            if (!string.isNullOrEmpty()) {
                binding.chipGroup.addView(Chip(context).apply {
                    text = string
                    this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                    this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용

                    this.setCloseIconResource(R.drawable.ic_friend_btn_remove) //TODO closebtn (이게 적용이 안되고 있음)

                    binding.autoEtToWho.setText(null)
                    this.setCloseIconVisible(true)
                    setOnCloseIconClickListener { binding.chipGroup.removeView(this) }
                })
            }
        }

        binding.ivTitleClear.setOnClickListener {
            binding.etTitle.setText(null)
            binding.ivTitleClear.visibility = View.INVISIBLE
        }
    }

    private fun initAutiComletetv() {
        // 리스트를 생성한다.
        friendlist = ArrayList()

        // 리스트에 검색될 데이터(단어)를 추가한다.
        settingList()

        val autoCompleteTextView =
            binding.autoEtToWho

        // AutoCompleteTextView 에 어댑터를 연결한다.
        var adapter = activity?.let {
            ArrayAdapter(
                it, R.layout.item_apply_search_friend, R.id.tv_apply_name,
                friendlist as ArrayList<String>
            )
        }
        autoCompleteTextView.setAdapter(adapter)

    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private fun settingList() {
        friendlist!!.add("박수빈")
        friendlist!!.add("박지현")
        friendlist!!.add("박승윤")
        friendlist!!.add("박나은")
        friendlist!!.add("박주혁")
        friendlist!!.add("박예림")
        friendlist!!.add("박한빈")
        friendlist!!.add("박수현")
    }

    fun initFocusBackground() {
        binding.etTitle.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10_stroke_pink)
                binding.etTitle.setHint(null)

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
        binding.autoEtToWho.addTextChangedListener {
            if (isNullOrBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }
        binding.etTitle.addTextChangedListener {//TODO 버그 발견: 제목 tv 클릭하고 아무것도 안 입력하고 내용 tv 누르면 제목 hint 사라짐
            if (binding.etTitle.text.isNullOrBlank()) { //공백일 때
                binding.ivTitleClear.visibility = View.INVISIBLE
                binding.etTitle.setHint("제목")
            } else { //뭐가 있을 때
                binding.ivTitleClear.visibility = View.VISIBLE
            }
            if (isNullOrBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }
        binding.etDetail.addTextChangedListener {
            if (isNullOrBlank()) {
                unactiveBtn()
            } else {
                activeBtn()
            }
        }
    }

    private fun isNullOrBlank(): Boolean {
        return binding.chipGroup.childCount == 0 || binding.etTitle.text.isNullOrBlank() || binding.autoEtToWho.text.isNullOrBlank()
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