package org.seemeet.seemeet.ui.send.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class SendItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view)
        return if (holder is SendInvitationAdapter.SendInviViewHolder) {
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
}