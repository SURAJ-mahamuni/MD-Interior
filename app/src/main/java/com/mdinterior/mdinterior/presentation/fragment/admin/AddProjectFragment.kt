package com.mdinterior.mdinterior.presentation.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentAddProjectBinding
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProjectFragment : BindingFragment<FragmentAddProjectBinding>() {
    override val backPressedHandler: () -> Unit
        get() = { findNavController().navigateUp() }
    override val onCreate: () -> Unit
        get() = {}
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAddProjectBinding::inflate

}