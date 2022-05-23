package org.seemeet.seemeet.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentSecondMyPageBinding
import org.seemeet.seemeet.ui.apply.FirstApplyFragment

class SecondMyPageFragment : Fragment() {

    private var _binding: FragmentSecondMyPageBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondMyPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)


        binding.btnCancel.setOnClickListener{
            getActivity()!!.getSupportFragmentManager().beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}