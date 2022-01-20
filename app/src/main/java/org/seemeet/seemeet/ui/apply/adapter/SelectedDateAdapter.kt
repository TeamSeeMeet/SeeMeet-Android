package org.seemeet.seemeet.ui.apply.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.StartEndDateData
import org.seemeet.seemeet.databinding.ItemSelectedDateBinding
import org.seemeet.seemeet.util.TimeParsing

class SelectedDateAdapter() : RecyclerView.Adapter<SelectedDateAdapter.ViewHolder>() {
    private var deleteListener: (() -> Unit)? = null
    val selectedDateList = mutableListOf<StartEndDateData>()
    private var context: Context? = null

    fun setDeleteListener(listener: (() -> Unit)) {
        deleteListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedDateAdapter.ViewHolder {
        val binding = ItemSelectedDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: SelectedDateAdapter.ViewHolder, position: Int) {
        viewHolder.bind(selectedDateList[position], position)
    }

    override fun getItemCount(): Int = selectedDateList.size


    inner class ViewHolder(private val binding: ItemSelectedDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: StartEndDateData, position: Int) {
            binding.apply {
                startEndDate = date
                imgRemove.setOnClickListener {
                    removeItem(position)
                }
                tvStartTime.text = date.start.TimeParsing()
                tvEndTime.text = date.end.TimeParsing()
            }
        }
    }

    fun removeItem(position: Int) {
        selectedDateList.removeAt(position)
        deleteListener?.invoke()
        notifyDataSetChanged()

    }

    fun addItem(item: StartEndDateData) {
        selectedDateList.add(item)
        notifyDataSetChanged()
    }

}