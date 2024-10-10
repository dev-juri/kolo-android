package com.juri.kolo_android.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.juri.kolo_android.R
import com.juri.kolo_android.data.local.entities.DbUser
import com.juri.kolo_android.data.model.CreateFamilyBody
import com.juri.kolo_android.databinding.FragmentCreateFamilyBinding
import com.juri.kolo_android.presentation.viewmodels.FamilyViewModel
import com.juri.kolo_android.utils.DataState
import com.juri.kolo_android.utils.observeOnce
import com.juri.kolo_android.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFamilyFragment : Fragment(R.layout.fragment_create_family) {

    private val binding by viewBinding(FragmentCreateFamilyBinding::bind)

    private val viewModel : FamilyViewModel by viewModels()

    private lateinit var user: DbUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeStates()

        binding.createFamilyBtn.setOnClickListener {
            val name = binding.familyNameField.editText?.text.toString().trim()

            if (name.isNotEmpty()) {
                viewModel.createFamily(CreateFamilyBody(name), user.id)
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

        viewModel.user.observeOnce(viewLifecycleOwner) {
            user = it
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    DataState.SUCCESS -> {
                        binding.createFamilyBtn.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_createFamilyFragment_to_homeFragment)
                    }

                    DataState.ERROR -> {
                        binding.createFamilyBtn.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> {
                        binding.createFamilyBtn.isEnabled = false
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