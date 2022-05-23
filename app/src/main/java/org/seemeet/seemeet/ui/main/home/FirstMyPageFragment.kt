package org.seemeet.seemeet.ui.main.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentFirstMyPageBinding
import org.seemeet.seemeet.ui.apply.FirstApplyFragment
import org.seemeet.seemeet.ui.apply.SecondApplyActivity

class FirstMyPageFragment : Fragment() {

    private var _binding: FragmentFirstMyPageBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstMyPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)


        binding.button.setOnClickListener{
            getActivity()!!.getSupportFragmentManager().beginTransaction().add(R.id.container_mypage, SecondMyPageFragment()).commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}