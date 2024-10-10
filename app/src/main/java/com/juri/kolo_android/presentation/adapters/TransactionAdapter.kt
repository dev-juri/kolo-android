package com.juri.kolo_android.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juri.kolo_android.data.local.entities.DbTransactions
import com.juri.kolo_android.databinding.LayoutTransactionBinding
import com.juri.kolo_android.utils.currencyFormatterDecimal
import com.juri.kolo_android.utils.formatDateTime

class TransactionAdapter :
    ListAdapter<DbTransactions, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback) {

    companion object TransactionDiffCallback : DiffUtil.ItemCallback<DbTransactions>() {
        override fun areItemsTheSame(oldItem: DbTransactions, newItem: DbTransactions): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DbTransactions, newItem: DbTransactions): Boolean {
            return oldItem == newItem
        }

    }

    class TransactionViewHolder(private var binding: LayoutTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dbTransactions: DbTransactions) {
            binding.amount.text = currencyFormatterDecimal((dbTransactions.amount) / 100)
            binding.type.text = dbTransactions.type.uppercase()
            binding.status.text = dbTransactions.status.uppercase()
            binding.ref.text = dbTransactions.transactionRef
            binding.date.text = formatDateTime(dbTransactions.update)
        }

        companion object {
            fun from(parent: ViewGroup): TransactionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutTransactionBinding.inflate(layoutInflater, parent, false)
                return TransactionViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val dbTransactions = getItem(position)
        holder.bind(dbTransactions)
    }


}
