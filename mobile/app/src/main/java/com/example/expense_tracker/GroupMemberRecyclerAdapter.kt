package com.example.expense_tracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.expense_tracker.models.GroupMember

class GroupMemberRecyclerAdapter(private val memberList: List<GroupMember>?) :
    RecyclerView.Adapter<GroupMemberRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Giving the style for the layout - The layout really isn't designed for this but it's good enough.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)

        return ViewHolder(view)

    }

    @SuppressLint("SetTextI18n") // We do not bother with translation here
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Assigns values for each view holder.

        holder.nameView.text = memberList?.get(position)?.user?.name
        holder.nullView.text = "" // Otherwise it'd default to "Description".
        // The layout isn't really designed to have only one text source. The woes of close deadlines.

        //var item : Group = groupList.get(position)
        //holder?.cardView?.setOnClickListener { clickListener(item, position) }

    }

    override fun getItemCount(): Int {
        // Recycler View needs to know how many items are present
        if (memberList == null)
            return 0

        return memberList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView : TextView = itemView.findViewById(R.id.nameLabel)
        val nullView : TextView = itemView.findViewById(R.id.descLabel)
        //val cardView : CardView = itemView.findViewById(R.id.cardView)


    }

}