package org.seemeet.seemeet.ui.apply

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.data.model.response.friend.FriendListData
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding
import org.seemeet.seemeet.ui.apply.adapter.ApplyFriendAdapter
import org.seemeet.seemeet.ui.viewmodel.ApplyViewModel
import org.seemeet.seemeet.util.activeBtn
import org.seemeet.seemeet.util.inactiveBtn
import org.seemeet.seemeet.util.makeInVisible
import org.seemeet.seemeet.util.makeVisible

class FirstApplyFragment : Fragment() {

    private var _binding: FragmentFirstApplyBinding? = null
    val binding get() = _binding!!
    private lateinit var adapter: ApplyFriendAdapter
    var friendId: Int = 0
    var friendName: String? = null

    //로컬로 데이터 클래스 만들고 그걸로 intent 전달하기
    private var friendArr: ArrayList<ApplyFriendData> = arrayListOf<ApplyFriendData>()

    private val viewModel: ApplyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstApplyBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        arguments?.let {
            friendId = it.getInt("id")
            friendName = it.getString("name").toString()
        }

        initClickListener()
        setFriendObserver()
        initAutoCompletetv()

        return binding.root
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            val intent = Intent(context, SecondApplyActivity::class.java)
            intent.putExtra("chipFriendData", friendArr)
            intent.putExtra("title", binding.etTitle.text.toString())
            intent.putExtra("Desc", binding.etDetail.text.toString())
            startActivity(intent)
            requireActivity().finish()
            /*
            binding.chipGroup.children.iterator().forEach {
                Log.d("*****************CHIPGROUP_CHILD", "${it.id}")
            }*/
        }

        binding.ivTitleClear.setOnClickListener {
            binding.etTitle.text = null
            binding.ivTitleClear.makeInVisible()
        }

        binding.etToWho.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.rvFriend.makeVisible()
                if (binding.chipGroup.childCount == 0) {
                    viewModel.requestFriendList() //딱 클릭했을 때 친구 리스트 통신 시작
                }
            } else {
                binding.rvFriend.makeInVisible()
            }
        }

        binding.etTitle.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10_stroke_pink)

            } else {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray01_10)
            }
        }

        binding.etDetail.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10_stroke_pink)
            } else {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray01_10)
            }
        }

        binding.etToWho.addTextChangedListener {
            if (isEmpty()) { //셋 중 하나라도 작성 안 됐을 때
                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else { //셋 다 작성했을 때
                binding.btnNext.activeBtn()
            }
            adapter.setSearchWord(binding.etToWho.text.toString())

            if (binding.etToWho.text.isNullOrBlank()) {
                binding.tvWho.makeVisible()
            } else binding.tvWho.makeInVisible()
        }

        binding.etTitle.addTextChangedListener {
            if (binding.etTitle.text.isNullOrBlank()) { //공백일 때
                binding.ivTitleClear.makeInVisible()
            } else {
                binding.ivTitleClear.makeVisible()
            }
            if (isEmpty()) {
                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else {
                binding.btnNext.activeBtn()
            }
        }

        binding.etDetail.addTextChangedListener {
            if (isEmpty()) {
                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else {
                binding.btnNext.activeBtn()
            }
        }

        binding.etDetail.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                binding.svFirstApply.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })
    }

    private fun initAutoCompletetv() {

        adapter = ApplyFriendAdapter()
        binding.rvFriend.adapter = adapter
        adapter.notifyDataSetChanged()

        //키보드에서 완료 버튼 누르면
        binding.etToWho.setOnEditorActionListener { textView, i, keyEvent ->
            if (!binding.etToWho.text.isNullOrBlank()) {
                binding.etToWho.text.clear()
            }
            binding.tvWho.makeVisible()
            binding.etToWho.clearFocus()
            binding.rvFriend.makeInVisible()
            false
        }

        if (friendName != null) { //친구 목록에서 버튼 클릭해서 들어왔을 경우
            viewModel.requestFriendList() //친구 리스트 통신 시작
            binding.chipGroup.addView(
                (layoutInflater.inflate(
                    R.layout.chip_layout,
                    null,
                    false
                ) as Chip).apply {
                    text = friendName
                    id = friendId

                    friendArr?.add(ApplyFriendData(friendId, friendName!!))

                    //Log.d("**********CHIP_111", "$id, $text")

                    this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                    this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                    binding.etToWho.text.clear()
                    binding.etToWho.setPadding(binding.etToWho.paddingLeft + 95, 0, 0, 0)

                    setOnCloseIconClickListener {
                        binding.chipGroup.removeView(this)
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft - 95, 0, 0, 0)
                        if (isEmpty()) { //셋 중 하나라도 작성 안 됐을 때
                            binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
                        } else { //셋 다 작성했을 때
                            binding.btnNext.activeBtn()
                        }
                        adapter.addItem(
                            FriendListData("0", 0, friendName!!) //todo: email, id값 수정해야함
                        )
                        adapter.sortItem(
                            FriendListData("0", 0, friendName!!)
                        )
                        if (binding.chipGroup.childCount < 3) {
                            binding.etToWho.isEnabled = true
                        }

                        //Log.d("********REMOVE_ID", this.id.toString())

                        val fd = friendArr.filter { it.id == this.id }

                        friendArr.remove(fd[0])

                    }
                })
            //adapter.removeItem(1)
        }

        //아이템을 클릭했을 때
        adapter.setOnItemClickListener { friendData ->
            if (binding.chipGroup.childCount < 3) {
                if (binding.chipGroup.childCount == 2) {
                    binding.etToWho.clearFocus()
                }
                friendName = friendData.username
                friendId = friendData.id.toInt()

                binding.chipGroup.addView(
                    (layoutInflater.inflate(
                        R.layout.chip_layout,
                        null,
                        false
                    ) as Chip).apply {
                        text = friendData.username //name 받아온 것
                        id = friendData.id //*******************test

                        friendArr?.add(ApplyFriendData(friendData.id, friendData.username))

                        Log.d("**********CHIP_111", "$id, $text")

                        this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                        this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                        binding.etToWho.text.clear()
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft + 95, 0, 0, 0)
                        setOnCloseIconClickListener {
                            binding.chipGroup.removeView(this)
                            binding.etToWho.setPadding(binding.etToWho.paddingLeft - 95, 0, 0, 0)
                            if (isEmpty()) {
                                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
                            } else {
                                binding.btnNext.activeBtn()
                            }
                            adapter.addItem(
                                friendData
                            )
                            adapter.sortItem(
                                friendData
                            )
                            if (binding.chipGroup.childCount < 3) {
                                binding.etToWho.isEnabled = true
                            }

                            val fd = friendArr.filter { it.id == this.id }
                            friendArr.remove(fd[0])
                        }
                    })
                adapter.removeItem(adapter.getPosition())
            }

            if (isEmpty()) { //셋 중 하나라도 작성 안 됐을 때
                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else { //셋 다 작성했을 때
                binding.btnNext.activeBtn()
            }
        }
    }

    fun setFriendObserver() {
        viewModel.friendList.observe(this, Observer { friendList ->
            Log.d("*************APPLY_FreindList", friendList[0].username)

            friendList.forEach {
                Log.d("*******CHIP_152", "${it.id}, ${it.username}")
            }

            with(binding.rvFriend.adapter as ApplyFriendAdapter) {
                setFriend(friendList)
            }

        })
    }

    //여기서 하나라도 성립하면 true 반환
    private fun isEmpty(): Boolean {
        return binding.chipGroup.childCount == 0 ||
                binding.etTitle.text.isNullOrBlank() ||
                binding.etDetail.text.isNullOrBlank() ||
                !binding.etToWho.text.isNullOrBlank()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}