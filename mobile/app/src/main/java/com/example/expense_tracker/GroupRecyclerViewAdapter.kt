package com.example.expense_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.models.Group

class GroupRecyclerViewAdapter(private val groupList: List<Group>, val clickListener: (Group, Int) -> Unit) :
    RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Giving the style for the layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)

        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Assigns values for each view holder.

        holder.nameView.text = groupList[position].name
        holder.descView.text = groupList[position].description

        val item : Group = groupList[position]
        holder.cardView.setOnClickListener { clickListener(item, position) }

    }

    override fun getItemCount(): Int {
        // Recycler View needs to know how many items are present
        return groupList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView : TextView = itemView.findViewById(R.id.nameLabel)
        val descView : TextView = itemView.findViewById(R.id.descLabel)
        val cardView : CardView = itemView.findViewById(R.id.cardView)


    }

}