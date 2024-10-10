package com.juri.kolo_android.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.juri.kolo_android.R
import com.juri.kolo_android.data.model.LoginBody
import com.juri.kolo_android.databinding.FragmentLoginBinding
import com.juri.kolo_android.presentation.viewmodels.AuthViewModel
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.login.setOnClickListener {
            val email = binding.emailAddress.editText?.text.toString().trim().lowercase()
            val password = binding.passwordField.editText?.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(LoginBody(email, password))
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
                        binding.login.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        this.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                    DataState.ERROR -> {
                        binding.login.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> {
                        binding.login.isEnabled = false
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