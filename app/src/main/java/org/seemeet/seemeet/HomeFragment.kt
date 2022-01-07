package org.seemeet.seemeet

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import org.seemeet.seemeet.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        initNaviDrawer()
    }


    private fun initClickListener(){
        binding.ivHomeFriend.setOnClickListener {
            FriendActivity.start(requireContext())
        }

        binding.ivMypageMenu.setOnClickListener{
            binding.dlHomeMypage.openDrawer(GravityCompat.START)
        }
        binding.nvMypage.ivMypageBack.setOnClickListener {
            binding.dlHomeMypage.closeDrawer(GravityCompat.START)
        }
    }

    private fun initNaviDrawer(){
        val toggle = ActionBarDrawerToggle(
            this.activity, binding.dlHomeMypage, R.string.home_drawer_open, R.string.home_drawer_close
        )

        binding.dlHomeMypage.addDrawerListener(toggle)
        toggle.syncState()

    }
}