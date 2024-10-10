package com.juri.kolo_android.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.juri.kolo_android.R
import com.juri.kolo_android.data.local.entities.DbUser
import com.juri.kolo_android.data.model.DepositBody
import com.juri.kolo_android.databinding.FragmentDepositBinding
import com.juri.kolo_android.presentation.viewmodels.TransactionsViewModel
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFragment : Fragment(R.layout.fragment_deposit) {

    private val binding by viewBinding(FragmentDepositBinding::bind)

    private val viewModel: TransactionsViewModel by viewModels()

    private lateinit var user: DbUser

    private val navArgs by navArgs<DepositFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeStates()

        binding.depositBtn.setOnClickListener {
            val amount = binding.amountField.editText?.text.toString().trim()

            if (amount.isNotEmpty()) {
                viewModel.deposit(DepositBody(user.id, navArgs.familyId, amount.toDouble()))
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

        viewModel.authorizationUrl.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController()
                    .navigate(DepositFragmentDirections.actionDepositFragmentToCheckoutFragment(it))
                viewModel.reset()
            }
        }
        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    DataState.SUCCESS -> {
                        binding.depositBtn.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                    }

                    DataState.ERROR -> {
                        binding.depositBtn.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> {
                        binding.depositBtn.isEnabled = false
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