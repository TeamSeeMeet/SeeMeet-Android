package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.internal.bind.ArrayTypeAdapter
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.FriendData
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding
import java.util.ArrayList


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

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_firstApplyFragment_to_secondApplyFragment)
        }

        // 리스트를 생성한다.
        friendlist = ArrayList()

        // 리스트에 검색될 데이터(단어)를 추가한다.
        settingList()
        val autoCompleteTextView =
            binding.autoEtToWho as AutoCompleteTextView

        // AutoCompleteTextView 에 아답터를 연결한다.
        var adapter = activity?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_dropdown_item_1line,
                friendlist as ArrayList<String>
            )}
        autoCompleteTextView.setAdapter(adapter)

        initFocusBackground()
        initTextChangedListener()

        return binding.root
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
        binding.autoEtToWho.addTextChangedListener(object : TextWatcher {
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
        return binding.etDetail.text.isNullOrBlank() || binding.etId.text.isNullOrBlank() || binding.autoEtToWho.text.isNullOrBlank()
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