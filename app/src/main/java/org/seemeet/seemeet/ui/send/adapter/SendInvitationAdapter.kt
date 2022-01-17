package org.seemeet.seemeet.ui.send.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import org.seemeet.seemeet.data.local.InviData
import org.seemeet.seemeet.databinding.ItemSendTimelistBinding

class SendInvitationAdapter : RecyclerView.Adapter<SendInvitationAdapter.SendInviViewHolder>(){

    private var inviList = emptyList<InviData>()
    /*
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }*/

    class SendInviViewHolder(
        private val binding : ItemSendTimelistBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(data : InviData){
            binding.inviData = data
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendInviViewHolder {
        val binding = ItemSendTimelistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SendInviViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SendInviViewHolder, position: Int) {

        holder.bind(inviList[position])

       /* tracker?.let {
            holder.bind(data, it.isSelected(position.toLong()))
        }*/
    }

    override fun getItemCount(): Int = inviList.size

    override fun getItemId(position: Int): Long = position.toLong()


    fun setInviList(inviList : List<InviData>){
        this.inviList = inviList
        notifyDataSetChanged()
    }


    /*class SelectionKeyProvider(private val recyclerView: RecyclerView) : ItemKeyProvider<Long>(SCOPE_MAPPED) {

        override fun getKey(position: Int): Long {
            val holder = recyclerView.findViewHolderForAdapterPosition(position)
            return holder?.itemId ?: throw IllegalStateException("No Holder")
        }

        override fun getPosition(key: Long): Int {
            val holder = recyclerView.findViewHolderForItemId(key)
            return if (holder is SendInviViewHolder) {
                holder.bindingAdapterPosition
            } else {
                RecyclerView.NO_POSITION
            }
        }
    }*/


   /* class SelectionDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null

            val holder = recyclerView.getChildViewHolder(view)
            return if (holder is SendInviViewHolder) {
                object : ItemDetails<Long>() {
                    override fun getPosition(): Int {
                        return holder.bindingAdapterPosition
                    }

                    override fun getSelectionKey(): Long {
                        return holder.itemId
                    }
                }
            } else {
                null
            }
        }
    }*/

    /*class SelectionPredicate(private val recyclerView: RecyclerView) : SelectionTracker.SelectionPredicate<Long>() {
        override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
            return true
        }

        override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
            return true
        }

        override fun canSelectMultiple(): Boolean {
            return false
        }
    }
*/


}