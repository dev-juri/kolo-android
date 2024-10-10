package com.juri.kolo_android.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.juri.kolo_android.R
import com.juri.kolo_android.databinding.FragmentExploreBinding
import com.juri.kolo_android.utils.viewBinding

class ExploreFragment: Fragment(R.layout.fragment_explore) {

    private val binding by viewBinding(FragmentExploreBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createFamily.setOnClickListener {
            this.findNavController().navigate(R.id.action_exploreFragment_to_createFamilyFragment)
        }

        binding.joinFamily.setOnClickListener {
            this.findNavController().navigate(R.id.action_exploreFragment_to_joinFamilyFragment)
        }
    }
}