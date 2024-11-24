package com.example.expense_tracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.databinding.ItemGroupBinding
import com.example.expense_tracker.models.Group

class GroupRecyclerViewAdapter(
    private var groups: List<Group>,
    private val onItemClick: (Group, Int) -> Unit
) : RecyclerView.Adapter<GroupRecyclerViewAdapter.GroupViewHolder>() {

    inner class GroupViewHolder(private val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group, position: Int) {
            binding.groupNameTextView.text = group.name
            binding.groupDescriptionTextView.text = group.description ?: "No description"

            binding.root.setOnClickListener {
                onItemClick(group, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position], position)
    }

    override fun getItemCount(): Int = groups.size

    fun updateGroups(newGroups: List<Group>) {
        this.groups = newGroups
        notifyDataSetChanged()
    }
}
