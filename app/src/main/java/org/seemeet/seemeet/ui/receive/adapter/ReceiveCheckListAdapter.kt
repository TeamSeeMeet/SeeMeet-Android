package org.seemeet.seemeet.ui.receive.adapter

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.CheckboxData
import org.seemeet.seemeet.databinding.ItemReceiveCheckboxBinding
import android.widget.CompoundButton

import android.content.ClipData.Item
import org.seemeet.seemeet.R


class ReceiveCheckListAdapter : RecyclerView.Adapter<ReceiveCheckListAdapter.RecieveTimeListViewHolder>() {

    private var checkboxList = emptyList<CheckboxData>()



    class RecieveTimeListViewHolder(
        private val binding : ItemReceiveCheckboxBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(data: CheckboxData){
            /*더미데이터 어캐 넘기지. 으으으ㅡ으음으음 으으음 으으음~~~~`
            일단 하고 나중에 수정하기....
            일단 그냥 이쁘게 string 값해서 넘겨주고, 나중에 bindingAdapter에서 변환을 하든 자르든 하자.
           */
            binding.checkboxData = data

            if(!data.flag){
                binding.cbReceive.isEnabled = false
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecieveTimeListViewHolder {
        val binding = ItemReceiveCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return  RecieveTimeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder:  RecieveTimeListViewHolder, position: Int) {
        holder.bind(checkboxList[position])
    }

    override fun getItemCount(): Int = checkboxList.size

    fun setCheckBox(checkboxList : List<CheckboxData>){
        this.checkboxList = checkboxList
        notifyDataSetChanged()
    }

}