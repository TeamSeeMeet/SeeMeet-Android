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
    var friendId: Int = -1
    var friendName: String? = null
    var friendPos: Int = -1
    var friendEmail: String? = null

    //로컬 데이터 클래스 만든걸로 intent 전달하기
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
            friendPos = it.getInt("pos")
            friendEmail = it.getString("email").toString()
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
        }

        binding.ivTitleClear.setOnClickListener {
            binding.etTitle.text = null
            binding.ivTitleClear.makeInVisible()
        }

        binding.etToWho.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.rvFriend.makeVisible()
                if (binding.chipGroup.childCount == 0) {
                    viewModel.requestFriendList() //chip 개수가 0이면서 포커스 눌렀을 때  친구 리스트 통신 시작
                    friendPos = -1 //친구 목록에서 버튼 클릭해서 들어왔는데 바로 그 칩 하나를 삭제하고 포커스 눌렀을 경우 버그 해결
                } else {
                    if (friendPos != -1) {
                        adapter.removeItem(friendPos)
                        friendPos = -1
                    }
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
            if (isEmpty()) { //필요한 내용 중 하나라도 작성 안 됐을 때
                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else { //필요한 내용 모두 작성했을 때
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

        //키보드에서 완료 버튼 누르는 이벤트
        binding.etToWho.setOnEditorActionListener { textView, i, keyEvent ->
            if (!binding.etToWho.text.isNullOrBlank()) {
                binding.etToWho.text.clear()
            }
            binding.tvWho.makeVisible()
            binding.etToWho.clearFocus()
            binding.rvFriend.makeInVisible()
            false
        }

        //친구 목록에서 버튼 클릭해서 들어왔을 경우
        if (friendName != null) {
            viewModel.requestFriendList() //친구 리스트 불러오는 서버 통신 시작
            //Chip 1개 생성
            binding.chipGroup.addView(
                (layoutInflater.inflate(
                    R.layout.chip_layout,
                    null,
                    false
                ) as Chip).apply {
                    text = friendName
                    id = friendId

                    friendArr?.add(ApplyFriendData(friendId, friendName!!))

                    this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                    this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                    binding.etToWho.text.clear()
                    binding.etToWho.setPadding(binding.etToWho.paddingLeft + 95, 0, 0, 0)

                    //Chip의 x버튼 누를 경우
                    setOnCloseIconClickListener {
                        binding.chipGroup.removeView(this)
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft - 95, 0, 0, 0)
                        if (isEmpty()) {
                            binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
                        } else {
                            binding.btnNext.activeBtn()
                        }
                        Log.d(
                            "*******FRIEND INFO********",
                            friendEmail.toString() + friendId.toString() + friendName
                        )
                        adapter.addItem(
                            FriendListData(friendEmail.toString(), friendId, friendName!!)
                        )
                        adapter.sortItem(
                            FriendListData(friendEmail.toString(), friendId, friendName!!)
                        )
                        if (binding.chipGroup.childCount < 3) {
                            binding.etToWho.isEnabled = true
                        }
                        //SecondApplyActivity로 넘겨줄 리스트에서 제거
                        val fd = friendArr.filter { it.id == this.id }
                        friendArr.remove(fd[0])
                    }
                })
        }

        //RecyclerView의 아이템 하나를 클릭했을 때
        adapter.setOnItemClickListener { friendData ->
            if (binding.chipGroup.childCount < 3) {
                //Chip 3개가 꽉 차면 focus 나가기
                if (binding.chipGroup.childCount == 2) {
                    binding.etToWho.clearFocus()
                }
                //Chip 1개 생성
                binding.chipGroup.addView(
                    (layoutInflater.inflate(
                        R.layout.chip_layout,
                        null,
                        false
                    ) as Chip).apply {
                        text = friendData.username
                        id = friendData.id

                        friendArr?.add(ApplyFriendData(friendData.id, friendData.username))

                        this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                        this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                        binding.etToWho.text.clear()
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft + 95, 0, 0, 0)

                        //Chip의 x버튼 누를 경우
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
                            //SecondApplyActivity로 넘겨줄 리스트에서 제거
                            val fd = friendArr.filter { it.id == this.id }
                            friendArr.remove(fd[0])
                        }
                    })
                adapter.removeItem(adapter.getPosition())
            }

            if (isEmpty()) {
                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
            } else {
                binding.btnNext.activeBtn()
            }
        }
    }

    fun setFriendObserver() {
        viewModel.friendList.observe(this, Observer { friendList ->
            //friendList.forEach { Log.d("*******CHIP_152", "${it.id}, ${it.username}") }
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