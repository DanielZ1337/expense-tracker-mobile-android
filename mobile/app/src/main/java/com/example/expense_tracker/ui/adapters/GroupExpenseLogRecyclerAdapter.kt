package com.example.expense_tracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expense_tracker.databinding.ItemGroupExpenseBinding
import com.example.expense_tracker.models.GroupExpense

class GroupExpenseLogRecyclerAdapter(
    private var expenses: List<GroupExpense>
) : RecyclerView.Adapter<GroupExpenseLogRecyclerAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(private val binding: ItemGroupExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: GroupExpense) {
            binding.expenseNameTextView.text = expense.expense?.title ?: "Expense title"
            binding.expenseAmountTextView.text = "$${expense.expense?.amount}"
            binding.expenseDateTextView.text = expense.createdAt.toString() // Format as needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemGroupExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(expenses[position])
    }

    override fun getItemCount(): Int = expenses.size

    fun updateExpenses(newExpenses: List<GroupExpense>) {
        this.expenses = newExpenses
        notifyDataSetChanged()
    }
}
