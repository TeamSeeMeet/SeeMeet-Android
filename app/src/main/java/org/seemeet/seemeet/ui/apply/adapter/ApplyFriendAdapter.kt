package org.seemeet.seemeet.ui.apply.adapter

import android.content.Context
import org.seemeet.seemeet.data.local.ApplyFriendData
import org.seemeet.seemeet.data.local.FriendData
import java.util.ArrayList
import androidx.annotation.NonNull

import android.R
import android.icu.util.UniversalTimeScale.toLong

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import org.seemeet.seemeet.databinding.FragmentFirstApplyBinding
import org.seemeet.seemeet.databinding.ItemApplySearchFriendBinding


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

/*
class ApplyFriendAdapter(context: Context, countryList: List<ApplyFriendData?>) :
    ArrayAdapter<ApplyFriendData?>(context, 0, countryList) {
    //데이터를 넣을 리스트
    //private val countryListFull: List<ApplyFriendData>
    private var _binding: ItemApplySearchFriendBinding? = null
    val binding get() = _binding!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                org.seemeet.seemeet.R.layout.item_apply_search_friend, parent, false
            )
        }

        //getItem(position) 코드로 자동완성 될 아이템을 가져온다
        val countryItem: ApplyFriendData? = getItem(position)
        if (countryItem != null) {
            binding.tvApplyName.setText(countryItem.name)
            binding.ivApplyProfile.setImageResource(countryItem.profile)
        }
        return convertView!!
    }
}
*/

/*
    //-------------------------- 이 아래는 자동완성을 위한 검색어를 찾아주는 코드이다 --------------------------
    override fun getFilter(): Filter {
        return countryFilter
    }

    private val countryFilter: Filter = object : Filter() {
        protected fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions: MutableList<ApplyFriendData> = ArrayList<ApplyFriendData>()
            if (constraint == null || constraint.length == 0) {
                suggestions.addAll(countryListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in countryListFull) {
                    if (item.getCountryName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item)
                    }
                }
            }
            results.values = suggestions
            results.count = suggestions.size
            return results
        }

        protected fun publishResults(constraint: CharSequence?, results: FilterResults) {
            clear()
            addAll(results.values as List<*>)
            notifyDataSetChanged()
        }

        fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as ApplyFriendData).getCountryName()
        }
    }

    init {
        countryListFull = ArrayList<Any?>(countryList)
    }
}*/
/*
class PoiAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allPois: List<ApplyFriendData>):
    ArrayAdapter<ApplyFriendData>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<ApplyFriendData> = allPois

    override fun getCount(): Int {
        return mPois.size
    }

    override fun getItem(p0: Int): ApplyFriendData? {
        return mPois.get(p0)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = "${mPois[position].name}"
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mPois = filterResults.values as List<ApplyFriendData>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.name.toLowerCase().contains(queryString)
                    }
                return filterResults
            }
        }
    }
}*/