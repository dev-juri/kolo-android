package com.juri.kolo_android.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.juri.kolo_android.R
import com.juri.kolo_android.data.model.RegisterBody
import com.juri.kolo_android.databinding.FragmentSignupBinding
import com.juri.kolo_android.presentation.viewmodels.AuthViewModel
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private val binding by viewBinding(FragmentSignupBinding::bind)

    private val viewModel : AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup.setOnClickListener {
            val email = binding.emailAddress.editText?.text.toString().trim().lowercase()
            val fullName = binding.nameField.editText?.text.toString().trim()
            var phoneNumber = binding.phoneField.editText?.text.toString().trim()
            val password = binding.passwordField.editText?.text.toString().trim()

            if(!phoneNumber.startsWith("+")) phoneNumber = phoneNumber.replaceFirst("0", "+234")

            if (email.isNotEmpty() && fullName.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty()) {
                viewModel.register(RegisterBody(fullName, email, phoneNumber, password))
            } else {
                Toast.makeText(
                    requireContext(),
                    "No field should be left blank",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        observeStates()
    }

    private fun observeStates() {

        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    DataState.SUCCESS -> {
                        binding.signup.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        this.findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                    }

                    DataState.ERROR -> {
                        binding.signup.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> {
                        binding.signup.isEnabled = false
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