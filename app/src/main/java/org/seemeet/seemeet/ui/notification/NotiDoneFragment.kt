package org.seemeet.seemeet.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.seemeet.seemeet.data.SeeMeetSharedPreference
import org.seemeet.seemeet.databinding.FragmentNotiDoneBinding
import org.seemeet.seemeet.ui.notification.adapter.NotiDoneListAdapter
import org.seemeet.seemeet.ui.viewmodel.NotiViewModel

class NotiDoneFragment : Fragment() {
    private var _binding: FragmentNotiDoneBinding? = null
    val binding get() = _binding!!

    private val viewmodel: NotiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiDoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SeeMeetSharedPreference.getLogin()) {
            viewmodel.requestAllInvitationList()

            setDoneAdapter()
            setDoneObserve()
        } else {
            binding.clNotiDoneNull.visibility = View.VISIBLE
            binding.tvDoneNum.text = "0"
        }
    }

    private fun setDoneAdapter() {
        val doneListAdapter = NotiDoneListAdapter()
        binding.rvDoneList.adapter = doneListAdapter
        binding.rvDoneList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.tvDoneNum.text = "${doneListAdapter.itemCount}"
    }

    private fun setDoneObserve() {
        viewmodel.invitationList.observe(viewLifecycleOwner) {
            viewmodel.setInviDoneList()
        }

        viewmodel.inviDoneList.observe(viewLifecycleOwner) { doneList ->
            with(binding.rvDoneList.adapter as NotiDoneListAdapter) {
                setDone(doneList)
                binding.tvDoneNum.text = doneList.size.toString()

                if (doneList.isEmpty()) {
                    binding.clNotiDoneNull.visibility = View.VISIBLE
                } else {
                    binding.clNotiDoneNull.visibility = View.GONE
                }
            }
        }
    }
}

