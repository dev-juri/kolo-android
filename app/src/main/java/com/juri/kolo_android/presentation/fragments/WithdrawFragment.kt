package com.juri.kolo_android.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.juri.kolo_android.R
import com.juri.kolo_android.data.local.entities.DbUser
import com.juri.kolo_android.data.model.WithdrawBody
import com.juri.kolo_android.databinding.FragmentWithdrawBinding
import com.juri.kolo_android.presentation.viewmodels.TransactionsViewModel
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawFragment : Fragment(R.layout.fragment_withdraw) {

    private val binding by viewBinding(FragmentWithdrawBinding::bind)

    private val viewModel: TransactionsViewModel by viewModels()

    private lateinit var user: DbUser

    private val navArgs by navArgs<WithdrawFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeStates()

        binding.withdrawBtn.setOnClickListener {
            val amount = binding.amountField.editText?.text.toString().trim()
            val name = binding.nameField.editText?.text.toString().trim()
            val bankName = binding.bankNameField.editText?.text.toString().trim()
            val acctNum = binding.acctNumField.editText?.text.toString().trim()
            val remarks = binding.remarksField.editText?.text.toString().trim()

            if (amount.isNotEmpty() && name.isNotEmpty() && acctNum.isNotEmpty()) {
                viewModel.withdraw(
                    WithdrawBody(
                        user.id,
                        name,
                        acctNum,
                        bankName,
                        remarks,
                        amount.toDouble(),
                        navArgs.familyId
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "No field should be left blank.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeStates() {

        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                user = it
            }
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    DataState.SUCCESS -> {
                        binding.withdrawBtn.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        this.findNavController().navigateUp()
                    }

                    DataState.ERROR -> {
                        binding.withdrawBtn.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> {
                        binding.withdrawBtn.isEnabled = false
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.errorTxt.apply {
                    text = it
                    visibility = View.VISIBLE
                }
            }
        }
    }
}