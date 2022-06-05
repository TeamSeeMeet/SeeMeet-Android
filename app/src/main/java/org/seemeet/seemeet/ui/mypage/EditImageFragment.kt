package org.seemeet.seemeet.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.FragmentEditImageBinding

class EditImageFragment : Fragment() {

    private var _binding: FragmentEditImageBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditImageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.tvProfileName.text = SeeMeetSharedPreference.getUserName()
        binding.tvProfileId.text = SeeMeetSharedPreference.getUserId()

        binding.btnSelectImage.setOnClickListener {
            //갤러리로 가기
        }

        binding.btnSaveProfile.setOnClickListener {
            //프로필 이미지 저장하는 서버 통신
        }

        binding.btnEdit.setOnClickListener {
            //안정해지긴했는데 이거 어때?
            getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}