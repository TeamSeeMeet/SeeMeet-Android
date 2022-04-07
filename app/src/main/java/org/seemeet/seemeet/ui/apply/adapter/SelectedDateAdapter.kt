package org.seemeet.seemeet.ui.apply.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.StartEndDateData
import org.seemeet.seemeet.data.model.response.calendar.CalendarEvent
import org.seemeet.seemeet.databinding.ItemSelectedDateBinding
import org.seemeet.seemeet.util.BindingRecyclerViewAdapter
import org.seemeet.seemeet.util.TimeParsing

class SelectedDateAdapter(private val deleteListener: (StartEndDateData, Int) -> Unit) : RecyclerView.Adapter<SelectedDateAdapter.ViewHolder>(),
    BindingRecyclerViewAdapter<List<StartEndDateData>> {
    var selectedDateList = mutableListOf<StartEndDateData>()
    private var context: Context? = null

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
                    removeItem(position,date)
                }
                tvStartTime.text = date.start.TimeParsing()
                tvEndTime.text = date.end.TimeParsing()
            }
        }
    }

    fun removeItem(position: Int,startEndDateData: StartEndDateData) {
        selectedDateList.removeAt(position)
        deleteListener(startEndDateData, selectedDateList.size)
        notifyDataSetChanged()

    }

    fun addItem(item: StartEndDateData) {
        selectedDateList.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setData(data: List<StartEndDateData>) {
        selectedDateList = data.toMutableList()
        notifyDataSetChanged()
    }
}