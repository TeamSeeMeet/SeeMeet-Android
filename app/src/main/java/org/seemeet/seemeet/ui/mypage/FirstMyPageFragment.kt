package org.seemeet.seemeet.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.FragmentFirstMyPageBinding

class FirstMyPageFragment : Fragment() {

    private var _binding: FragmentFirstMyPageBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstMyPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.tvFirstName.text = SeeMeetSharedPreference.getUserName()
        binding.tvFirstId.text = SeeMeetSharedPreference.getUserId()

        binding.btnEditProfile.setOnClickListener {
            //프로필 사진 편집하는 프래그먼트로
            getActivity()!!.getSupportFragmentManager().beginTransaction()
                .add(R.id.container_mypage, EditImageFragment()).commit()

        }

        binding.btnEdit.setOnClickListener {
            //이름, 아이디만 수정하는 프래그먼트로
            getActivity()!!.getSupportFragmentManager().beginTransaction()
                .add(R.id.container_mypage, EditNameIdFragment()).commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}