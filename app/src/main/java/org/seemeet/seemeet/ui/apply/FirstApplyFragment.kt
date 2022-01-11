package org.seemeet.seemeet.ui.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.seemeet.seemeet.MainActivity
import org.seemeet.seemeet.R
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding


class FirstApplyFragment : Fragment() {

    private var _binding: FragmentFirstApplyBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstApplyBinding.inflate(layoutInflater)

        binding.btnNext.setOnClickListener{
            findNavController().navigate(R.id.action_firstApplyFragment_to_secondApplyFragment)
        }
        return binding.root
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

}