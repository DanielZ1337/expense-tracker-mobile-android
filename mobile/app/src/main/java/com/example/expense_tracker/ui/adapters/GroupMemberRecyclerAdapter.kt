package com.example.expense_tracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.databinding.ItemGroupMemberBinding
import com.example.expense_tracker.models.GroupMember

class GroupMemberRecyclerAdapter(
    private var members: List<GroupMember>?
) : RecyclerView.Adapter<GroupMemberRecyclerAdapter.MemberViewHolder>() {

    inner class MemberViewHolder(private val binding: ItemGroupMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: GroupMember?) {
            binding.memberNameTextView.text = member?.user?.name ?: "User"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemGroupMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(members?.get(position))
    }

    override fun getItemCount(): Int = members?.size ?: 0

    fun updateMembers(newMembers: List<GroupMember>) {
        this.members = newMembers
        notifyDataSetChanged()
    }
}
