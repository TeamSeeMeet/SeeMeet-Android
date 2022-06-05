package org.seemeet.seemeet.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.FragmentEditNameIdBinding

class EditNameIdFragment : Fragment() {

    private var _binding: FragmentEditNameIdBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNameIdBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        binding.etMypageName.setText(SeeMeetSharedPreference.getUserName())
        binding.etMypageId.setText(SeeMeetSharedPreference.getUserId())

        binding.btnMypageCancel.setOnClickListener {
            getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this).commit()
        }

        binding.btnMypageSave.setOnClickListener {
            //서버 통신하고 다시 돌아와
            getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}