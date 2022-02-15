package org.seemeet.seemeet.ui.receive.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.model.response.invitation.ReceiveInvitationDate
import org.seemeet.seemeet.databinding.ItemReceiveCheckboxBinding

class ReceiveCheckListAdapter(  val onClickCheckbox : (receiveInvitationDate : ReceiveInvitationDate) -> Unit )
    : RecyclerView.Adapter<ReceiveCheckListAdapter.ReceiveTimeListViewHolder>() {

    private var checkboxList = emptyList<ReceiveInvitationDate>()
    private var isResponse = false

    class ReceiveTimeListViewHolder(
        private val binding : ItemReceiveCheckboxBinding
    ): RecyclerView.ViewHolder(binding.root){
        val cb : CheckBox
            get() {
                 return binding.cbReceive
            }

        fun bind(data: ReceiveInvitationDate){
            binding.checkboxData = data
            binding.cbReceive.isChecked = data.isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ReceiveTimeListViewHolder {
        val binding = ItemReceiveCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return  ReceiveTimeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder:  ReceiveTimeListViewHolder, position: Int) {
        holder.bind(checkboxList[position])

        holder.cb.setOnClickListener {
            checkboxList[position].isSelected = !checkboxList[position].isSelected
            onClickCheckbox.invoke(checkboxList[position])
            notifyItemChanged(position)
        }

        if(isResponse) {
            holder.cb.isEnabled = false
            holder.cb.isClickable = false
            holder.cb.setBackgroundResource(R.drawable.selector_receive_checkbox_unable)
        }
    }

    override fun getItemCount(): Int = checkboxList.size

    fun setCheckBox(checkboxList : List<ReceiveInvitationDate>){
        this.checkboxList = checkboxList
        notifyDataSetChanged()
    }

    fun getCheckBoxList() : List<ReceiveInvitationDate>{
        return checkboxList
    }

    fun setIsResponse(flag : Boolean) {
        isResponse = flag
    }


}