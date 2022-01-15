package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.util.Log
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
/*
    private var friendlist // 데이터를 넣은 리스트변수
            : MutableList<ApplyFriendData>? = null
 */

    private var friendlist // 데이터를 넣은 리스트변수
            : MutableList<String>? = null

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
        // 리스트를 생성한다.
        friendlist = ArrayList()

        // 리스트에 검색될 데이터(단어)를 추가한다.
        settingList()

        val autoCompleteTextView =
            binding.autoEtToWho

        // AutoCompleteTextView 에 어댑터를 연결한다.
/*
        var adapter = activity?.let {
            ApplyFriendAdapter(
                it,
                friendlist as List<ApplyFriendData>
            )
        }
*/

        var adapter = activity?.let {
            ArrayAdapter(
                it, R.layout.item_apply_search_friend, R.id.tv_apply_name,
                friendlist as List<String>
            )
        }


/*
        //커스텀 어댑터 생성하기
        var adapter = activity?.let{
            AutoCompleteAdapter(
                it, R.layout.item_apply_search_friend, R.id.tv_apply_name,
                friendlist as List<ApplyFriendData>
            )
        }

*/
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->

            val string = binding.autoEtToWho.text
            if (!string.isNullOrEmpty()) {
                binding.tvWho.visibility = View.VISIBLE
                binding.chipGroup.addView(Chip(context).apply {
                    text = string
                    this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                    this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용

                    //this.setCloseIconResource(R.drawable.ic_friend_btn_remove) //TODO closebtn (이게 적용이 안되고 있음)

                    binding.autoEtToWho.setText(null)
                    Log.d("test,",binding.autoEtToWho.paddingLeft.toString())
                    binding.autoEtToWho.setPadding(binding.autoEtToWho.paddingLeft + 250, 0, 0, 0)

                    this.setCloseIconVisible(true)
                    setOnCloseIconClickListener {
                        binding.chipGroup.removeView(this)
                        binding.autoEtToWho.setPadding(
                            binding.autoEtToWho.paddingLeft - 250,
                            0,
                            0,
                            0
                        )
                    }
                })
            }
        }
    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
/*
    private fun settingList() {
        friendlist!!.add(ApplyFriendData(1, "박유나"))
        friendlist!!.add(ApplyFriendData(1, "박지현"))
        friendlist!!.add(ApplyFriendData(1, "박승윤"))
        friendlist!!.add(ApplyFriendData(1, "박나은"))
        friendlist!!.add(ApplyFriendData(1, "박주혁"))
        friendlist!!.add(ApplyFriendData(1, "박예림"))
        friendlist!!.add(ApplyFriendData(1, "박한빈"))
        friendlist!!.add(ApplyFriendData(1, "박수현"))
    }*/


    private fun settingList() {
        friendlist!!.add("박유나")
        friendlist!!.add("박지현")
        friendlist!!.add("박승윤")
        friendlist!!.add("박나은")
        friendlist!!.add("박주혁")
        friendlist!!.add("박예림")
        friendlist!!.add("박한빈")
    }

    fun initFocusBackground() {
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
        binding.autoEtToWho.addTextChangedListener {
            if (binding.autoEtToWho.text.isNullOrBlank()) { //공백일 때
                binding.tvWho.visibility = View.VISIBLE

            } else { //뭐가 있을 때
                binding.tvWho.visibility = View.INVISIBLE
            }
            if (isNullorBlank()) { //셋 중 하나라도 작성 안 됐을 때
                unactiveBtn()
            } else { //셋 다 작성했을 때
                activeBtn()
            }
            Log.d("test,",binding.chipGroup.childCount.toString())
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
        return binding.chipGroup.childCount==0 || binding.etTitle.text.isNullOrBlank() || binding.etDetail.text.isNullOrBlank() || (!binding.autoEtToWho.text.isNullOrBlank()) //TODO 여기 chip 개수 !!!!
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