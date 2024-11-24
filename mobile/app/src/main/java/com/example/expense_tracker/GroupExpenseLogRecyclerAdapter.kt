package com.example.expense_tracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.models.Group
import com.example.expense_tracker.models.GroupExpense

class GroupExpenseLogRecyclerAdapter(private val expenseList: List<GroupExpense>) :
    RecyclerView.Adapter<GroupExpenseLogRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Giving the style for the layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)

        return ViewHolder(view)

    }

    @SuppressLint("SetTextI18n") // We do not bother with translation here
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Assigns values for each viewholder.

        holder.nameView.text = expenseList[position].expense?.payer?.name
        holder.paidView.text = " paid ${expenseList[position].expense?.amount} for expense ${expenseList[position].expense?.title}."

        //var item : Group = groupList.get(position)
        //holder?.cardView?.setOnClickListener { clickListener(item, position) }

    }

    override fun getItemCount(): Int {
        // Recycler View needs to know how many items are present
        return expenseList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView : TextView = itemView.findViewById(R.id.nameLabel)
        val paidView : TextView = itemView.findViewById(R.id.descLabel)
        val cardView : CardView = itemView.findViewById(R.id.cardView)


    }

}