package org.seemeet.seemeet.ui.apply.adapter

import android.content.Context
import android.widget.ArrayAdapter
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.data.local.FriendData
import java.util.ArrayList
import androidx.annotation.NonNull

import android.R

import android.widget.TextView

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.annotation.Nullable


/*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.R
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.databinding.ItemApplySearchFriendBinding

class ApplyFriendAdapter : RecyclerView.Adapter<ApplyFriendAdapter.ApplyFriendViewHolder>(){
    val applyfriendList = mutableListOf<ApplyFriendData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplyFriendViewHolder {
        val binding = ItemApplySearchFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplyFriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplyFriendViewHolder, position: Int) {
        holder.onBind(applyfriendList[position])
    }

    override fun getItemCount(): Int = applyfriendList.size

    class ApplyFriendViewHolder(private val binding: ItemApplySearchFriendBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data: ApplyFriendData) {
            binding.ivApplyProfile.setImageResource(R.drawable.ic_x)
            binding.tvApplyName.text = data.name
        }
    }
}
*/
/*
class ApplyFriendAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: List<ApplyFriendData>

) : ArrayAdapter<ApplyFriendData>(context, resource, textViewResourceId, objects) {

    private var suggests: MutableList<ApplyFriendData> = mutableListOf()

    fun setSuggests(list: List<ApplyFriendData>) {
        suggests.clear()
        suggests.addAll(list)
    }

    override fun getCount(): Int {
        return suggests.size
    }

    override fun getItem(position: Int): ApplyFriendData? {
        return suggests[position]
    }

}*/
/*
class ApplyFriendAdapter(
    private val mContext: Context,
    private val mLayoutResourceId: Int,
    friends: List<ApplyFriendData>
) :
    ArrayAdapter<ApplyFriendData>(mContext, mLayoutResourceId, friends) {
    private val friend: MutableList<ApplyFriendData> = ArrayList(friends)
    private var allCities: List<ApplyFriendData> = ArrayList(friends)

    override fun getCount(): Int {
        return friend.size
    }
    override fun getItem(position: Int): ApplyFriendData {
        return friend[position]
    }
    override fun getItemId(position: Int): Long {
        return friend[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = (mContext as Activity).layoutInflater
            convertView = inflater.inflate(mLayoutResourceId, parent, false)
        }
        try {
            val city: ApplyFriendData = getItem(position)
            val cityAutoCompleteView = convertView!!.findViewById<View>(R.id.autoCompleteTextViewItem) as TextView
            cityAutoCompleteView.text = city.definition
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return convertView!!
    }
}
*/
/*
class AutoCompleteAdapter(context: Context, countryList: List<ApplyFriendData?>) :
    ArrayAdapter<ApplyFriendData?>(context, 0, countryList) {
    private val countryListFull: List<ApplyFriendData>

    override fun getView(position: Int, @Nullable convertView: View, parent: ViewGroup): View {
        var convertView: View = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.country_autocomplete_row, parent, false
            )
        }
        val textView: TextView = convertView.findViewById(R.id.text_view_name)
        val imageView: ImageView = convertView.findViewById(R.id.image_view_flag)
        val countryItem: ApplyFriendData? = getItem(position)
        if (countryItem != null) {
            textView.setText(countryItem.getCountryName())
            imageView.setImageResource(countryItem.getFlagImage())
        }
        return convertView
    }

    init {
        countryListFull = ArrayList<Any?>(countryList)
    }
}*/