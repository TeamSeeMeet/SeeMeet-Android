package org.seemeet.seemeet.ui.apply

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import org.seemeet.seemeet.ui.viewmodel.BaseViewModel
import org.seemeet.seemeet.util.*
import retrofit2.HttpException

class FirstApplyFragment : Fragment() {

    private var _binding: FragmentFirstApplyBinding? = null
    val binding get() = _binding!!
    private lateinit var adapter: ApplyFriendAdapter
    private var friendId = -1
    private var friendPos = -1
    private var friendName: String? = null
    private var friendEmail: String? = null
    var err: Boolean = false    // 서버통신에 실패할 경우 err=true

    //SecondApplyActivity로 친구 Array 넘기기 위해
    private var friendArr: ArrayList<ApplyFriendData> = arrayListOf()
    private val viewModel: ApplyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstApplyBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        //ApplyActivity에서 값 받아오기
        arguments?.let {
            friendId = it.getInt("id")
            friendName = it.getString("name").toString()
            friendPos = it.getInt("pos")
            friendEmail = it.getString("email").toString()
        }
        setFriendObserver()
        initClickListener()
        makeChip()
        return binding.root
    }

    private fun setFriendObserver() {
        viewModel.friendList.observe(this, Observer { friendList ->
            with(binding.rvFriend.adapter as ApplyFriendAdapter) {
                setFriend(friendList)
            }
        })

        viewModel.fetchState.observe(this) {
            var message = ""
            when (it.second) {
                BaseViewModel.FetchState.BAD_INTERNET -> {
                    message = "소켓 오류 / 서버와 연결에 실패하였습니다."
                    err = true
                }
                BaseViewModel.FetchState.PARSE_ERROR -> {
                    val code = (it.first as HttpException).code()
                    message = "$code ERROR : \n ${it.first.message}"
                    err = true
                }
                BaseViewModel.FetchState.WRONG_CONNECTION -> {
                    message = "인터넷 연결을 확인해주세요"
                    setVisibility(true)
                    err = true
                }
                else -> {
                    message = "통신에 실패하였습니다.\n ${it.first.message}"
                    err = true
                }
            }

            Log.d("********NETWORK_ERROR_MESSAGE : ", it.first.message.toString())
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initClickListener() {

        //rvFriend 높이 키보드 뺸 높이만큼 setting
        setUpAsSoftKeyboard(binding.rvFriend)

        //키패드 내리는 버튼 누를 경우 rvFriend 사라지게
        binding.clFirstApply.viewTreeObserver.addOnGlobalLayoutListener {
            if (binding.etToWho.hasFocus()) {
                var mRootViewHeight = binding.clBg.rootView.height
                var mRelativeWrapperHeight = binding.clFirstApply.height
                if (mRootViewHeight - mRelativeWrapperHeight > dpToPx(200.0F)) {
                    binding.rvFriend.makeVisible()
                    setVisibility(false)
                } else {
                    binding.rvFriend.makeGone()
                    setVisibility(true)
                    //TODO: 포커스 나가게하기
                }
            }
        }

        binding.svFirstApply.setOnTouchListener { _, event ->
            val view = activity?.currentFocus
            if (view == binding.etToWho) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                val outRect2 = Rect()
                binding.rvFriend.getGlobalVisibleRect(outRect2)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt()) && !outRect2.contains(
                        event.rawX.toInt(),
                        event.rawY.toInt()
                    )
                ) {
                    view.clearFocus()
                    val imm =
                        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
                    setVisibility(true)
                }
            } else if (view != null && event != null) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm =
                        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }
            }
            false
        }

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

        binding.etToWho.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (binding.chipGroup.childCount == 3) {
                    setVisibility(true)
                    binding.etToWho.isEnabled = false
                    //todo 키보드 내리기
                } else {
                    setVisibility(false)
                }
                if (binding.chipGroup.childCount == 0) {
                    viewModel.requestFriendList() //chip 개수가 0이면서 포커스 눌렀을 때 친구 리스트 통신 시작
                    friendPos = -1 //친구 목록에서 버튼 클릭해서 들어왔는데 바로 그 칩 하나를 삭제하고 포커스 눌렀을 경우 버그 해결
                } else {
                    if (friendPos != -1 && !err) {
                        adapter.removeItem(friendPos)
                        friendPos = -1
                    }
                    if (binding.chipGroup.childCount == 1 && err)
                        viewModel.requestFriendList()
                }
            } else {
                setVisibility(true)
                if (!binding.etToWho.text.isNullOrBlank()) {
                    binding.etToWho.text.clear()
                }
            }
        }

        binding.etTitle.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray_10_stroke_pink)

            } else {
                binding.clContent.setBackgroundResource(R.drawable.rectangle_gray01_10)
            }
        }

        binding.etDetail.setOnFocusChangeListener { _, hasFocus ->
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
            if (binding.etTitle.text.isNullOrBlank()) {
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

        binding.etDetail.setOnTouchListener { _, _ ->
            binding.svFirstApply.requestDisallowInterceptTouchEvent(true)
            false
        }

        //키보드에서 완료 버튼 누르는 이벤트
        binding.etToWho.setOnEditorActionListener { _, _, _ ->
            if (!binding.etToWho.text.isNullOrBlank()) {
                binding.etToWho.text.clear()
            }
            binding.tvWho.makeVisible()
            binding.etToWho.clearFocus()
            setVisibility(true)
            false
        }
    }

    private fun makeChip() {
        adapter = ApplyFriendAdapter()
        binding.rvFriend.adapter = adapter
        adapter.notifyDataSetChanged()

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

                    friendArr.add(ApplyFriendData(friendId, friendName!!))

                    this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                    this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                    binding.etToWho.text.clear()
                    binding.etToWho.setPadding(binding.etToWho.paddingLeft + dpToPx(96.0F).toInt(), 0, 0, 0)

                    //Chip의 x버튼 누를 경우
                    setOnCloseIconClickListener {
                        binding.chipGroup.removeView(this)
                        binding.etToWho.isEnabled = true
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft - dpToPx(96.0F).toInt(), 0, 0, 0)
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
                            FriendListData(friendEmail.toString(), friendId, friendName!!, null)
                        )
                        adapter.sortItem()
                        if (binding.chipGroup.childCount < 3) {
                            binding.etToWho.isEnabled = true
                        }
                        //SecondApplyActivity로 넘겨줄 Array에서 제거
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
                    setPaddingEnd(binding.hsv, dpToPx(80.0F).toInt())
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

                        friendArr.add(ApplyFriendData(friendData.id, friendData.username))

                        this.setChipBackgroundColorResource(R.color.chipbackground) //container color
                        this.setTextAppearance(R.style.ChipTextAppearance) //글자 색, 글자 크기 적용
                        binding.etToWho.text.clear()
                        binding.etToWho.setPadding(binding.etToWho.paddingLeft + dpToPx(96.0F).toInt(), 0, 0, 0)

                        //Chip의 x버튼 누를 경우
                        setOnCloseIconClickListener {
                            binding.chipGroup.removeView(this)
                            binding.etToWho.isEnabled = true
                            binding.etToWho.setPadding(binding.etToWho.paddingLeft - dpToPx(96.0F).toInt(), 0, 0, 0)
                            if (isEmpty()) {
                                binding.btnNext.inactiveBtn(R.drawable.rectangle_gray02_10)
                            } else {
                                binding.btnNext.activeBtn()
                            }
                            adapter.addItem(
                                friendData
                            )
                            adapter.sortItem()
                            if (binding.chipGroup.childCount < 3) {
                                binding.etToWho.isEnabled = true
                                setPaddingEnd(binding.hsv, dpToPx(0.0F).toInt())
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

    //여기서 하나라도 성립하면 true 반환
    private fun isEmpty(): Boolean {
        return binding.chipGroup.childCount == 0 ||
                binding.etTitle.text.isNullOrBlank() ||
                binding.etDetail.text.isNullOrBlank() ||
                !binding.etToWho.text.isNullOrBlank() ||
                err
    }

    // flag=true 이면 rv만 보이고 뒤에 배경 다 안 보이게
    // flag=false 이면 rv 숨기고 뒤에 배경이 보이게
    private fun setVisibility(flag: Boolean) {
        if (flag) {
            binding.rvFriend.makeGone()
            binding.clContentBg.visibility = View.VISIBLE
            binding.btnNext.visibility = View.VISIBLE
        } else {
            binding.rvFriend.makeVisible()
            binding.clContentBg.visibility = View.GONE
            binding.btnNext.visibility = View.GONE
        }
    }

    fun setUpAsSoftKeyboard(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            targetViewHeight = getTargetViewHeight(view)

            // screenMaxHeight, keyboardHeight 초기화
            if (targetViewHeight > screenMaxHeight) screenMaxHeight = targetViewHeight
            else keyboardHeight = screenMaxHeight - targetViewHeight

            // 키보드 표시 여부에 따라 height 조정
            val param = view.layoutParams as ViewGroup.MarginLayoutParams
            param.height =
                targetViewHeight - keyboardHeight + getStatusBarHeightDP(view.context) + dpToPx(
                    58.0F
                ).toInt()
            view.layoutParams = param
        }
    }

    fun setPaddingEnd(view: View, end: Int){
        view.setPaddingRelative(0,0,end,0)
    }

    fun dpToPx(valueInDp: Float): Float {
        var metrics: DisplayMetrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
    }

    companion object {
        private var screenMaxHeight = 0
        private var keyboardHeight = 0
        var targetViewHeight = 0

        //현재 뷰 높이 구하는 함수
        private fun getTargetViewHeight(view: View): Int {
            val targetView = Rect()
            view.getWindowVisibleDisplayFrame(targetView)
            return targetView.height()
        }

        /* 상단 상태바 높이 구하는 함수
        계산 후 DP로 반환 */
        fun getStatusBarHeightDP(context: Context): Int {
            var result = 0
            val resourceId: Int =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimension(resourceId).toInt()
            }
            return result
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}