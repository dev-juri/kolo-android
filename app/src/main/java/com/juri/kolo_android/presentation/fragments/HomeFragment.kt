package com.juri.kolo_android.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.juri.kolo_android.R
import com.juri.kolo_android.data.local.entities.DbFamily
import com.juri.kolo_android.databinding.FragmentHomeBinding
import com.juri.kolo_android.presentation.adapters.TransactionAdapter
import com.juri.kolo_android.presentation.viewmodels.HomeViewModel
import com.juri.kolo_android.utils.currencyFormatterDecimal
import com.juri.kolo_android.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var family: DbFamily

    private lateinit var adapter: TransactionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TransactionAdapter()
        binding.txnRecycler.adapter = adapter

        viewModel.family.observe(viewLifecycleOwner) {
            if (it != null) {
                family = it
                binding.familyCode.text = it.familyCode
                binding.familyName.text = it.name
                binding.txtBal.text = currencyFormatterDecimal((it.balance) / 100)
                viewModel.fetchTxns(it.id)
            }
        }

        viewModel.transactions.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.txnEmptyState.visibility = View.GONE
                adapter.submitList(it)
            } else {
                adapter.submitList(emptyList())
                binding.txnEmptyState.visibility = View.VISIBLE
            }
        }

        binding.depositBtn.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDepositFragment(family.id))
        }

        binding.withdrawBtn.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWithdrawFragment(family.id))
        }
    }
}