package org.seemeet.seemeet.ui.apply

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.data.model.response.invitation.UserData
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding
import org.seemeet.seemeet.ui.apply.adapter.ApplyFriendAdapter
import org.seemeet.seemeet.ui.viewmodel.ApplyViewModel

class FirstApplyFragment : Fragment() {

    private var _binding: FragmentFirstApplyBinding? = null
    val binding get() = _binding!!
    private lateinit var adapter: ApplyFriendAdapter

    private var friendArr : ArrayList<ApplyFriendData> = arrayListOf<ApplyFriendData>()

    private val viewModel: ApplyViewModel by viewModels()
    //private var intentData: ApplyFriendData? = null
    //로컬로 데이터 클래스 만들고 그걸로 intent 전달하기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstApplyBinding.inflate(layoutInflater)

        initClickListener()
        initAutoCompletetv()
        setFriendObserver()
        initFocusBackground()
        initTextChangedListener()
        initScrollEvent()

        var id : Int= 0
        var name : String? = null
        super.onCreate(savedInstanceState)
        arguments?.let{
            id = it.getInt("id")
            name = it.getString("name").toString()
        }
        Log.d("%%%%%%%%", id.toString()+ name)

        return binding.root
    }


    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            // intetntData로 넘기지 말고 arraylist, title, desc 각가 따로 넘기기
           /* intentData?.desc = binding.etDetail.text.toString()
            intentData?.title = binding.etTitle.text.toString()*/

            val intent = Intent(context, SecondApplyActivity::class.java)
            intent.putExtra("chipFriendData",friendArr)
            intent.putExtra("title",binding.etTitle.text.toString())
            intent.putExtra("Desc", binding.etDetail.text.toString())
            startActivity(intent)
            requireActivity().finish()

            binding.chipGroup.children.iterator().forEach {
                Log.d("*****************CHIPGROUP_CHILD", "${it.id}")
            }



           /* intentData.guests = UserData(1,binding.chipGroup.children.toString())
            val intent = Intent(context, SecondApplyActivity::class.java)
            intent.putExtra("guests_id",) //데이터 넣기
            intent.putExtra("guests_name", friendList[1].userName)*/

           // intent.putExtra("title",binding.etTitle.text.toString())
           // intent.putExtra("Desc", binding.etDetail.text.toString())
            //intent.putExtra()
            //startActivity(intent)
            //SecondApplyActivity.start(requireContext())
        }

        binding.ivTitleClear.setOnClickListener {
            binding.etTitle.setText(null)
            binding.ivTitleClear.visibility = View.INVISIBLE
        }
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
            binding.tvWho.visibility = View.VISIBLE
            binding.etToWho.clearFocus()
            binding.rvFriend.visibility = View.INVISIBLE
            false
        }

        //아이템을 클릭했을 때
        adapter.setOnItemClickListener { friendData ->
            if (binding.chipGroup.childCount < 3) {
                if (binding.chipGroup.childCount == 2) {
                    binding.etToWho.clearFocus()
                }
                binding.chipGroup.addView(
                    (layoutInflater.inflate(
                        R.layout.chip_layout,
                        null,
                        false
                    ) as Chip).apply {
                        text = friendData.username //name 받아온 것
                        id= friendData.id //*******************test

                        friendArr?.add(ApplyFriendData(friendData.id, friendData.username))

                        Log.d("**********CHIP_111", "$id, $text")

                        this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                        this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                        binding.etToWho.text.clear()
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft + 260, 0, 0, 0)
                        setOnCloseIconClickListener {
                            binding.chipGroup.removeView(this)
                            binding.etToWho.setPadding(binding.etToWho.paddingLeft - 260, 0, 0, 0)
                            if (isNullorBlank()) { //셋 중 하나라도 작성 안 됐을 때
                                unactiveBtn()
                            } else { //셋 다 작성했을 때
                                activeBtn()
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

                            Log.d("********REMOVE_ID", this.id.toString())



                            /*for(i in  0 .. friendArr.size){
                                if(friendArr[i].userId == this.id)
                                    friendArr.remove(friendArr[i])
                            }*/

                            val fd = friendArr.filter{ it.id == this.id}
                            Log.d("************fd", fd[0].username.toString())

                            friendArr.remove(fd[0])

                        }
                    })
                adapter.removeItem(adapter.getPosition())
            }

            if (isNullorBlank()) { //셋 중 하나라도 작성 안 됐을 때
                unactiveBtn()
            } else { //셋 다 작성했을 때
                activeBtn()
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

    fun initFocusBackground() {
        binding.etToWho.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.rvFriend.visibility = View.VISIBLE
                if(binding.chipGroup.childCount==0) {
                    viewModel.requestFriendList() //딱 클릭했을 때 친구 리스트 통신 시작

                }
            } else {
                binding.rvFriend.visibility = View.INVISIBLE
                /*
                binding.applyAppointment.visibility = View.VISIBLE
                binding.applyContent.visibility = View.VISIBLE
                binding.applyWrite.visibility = View.VISIBLE
                binding.clContent.visibility = View.VISIBLE*/
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