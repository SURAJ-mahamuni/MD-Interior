package com.mdinterior.mdinterior.presentation.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.databinding.FragmentWelcomeBinding
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.viewModels.auth.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BindingFragment<FragmentWelcomeBinding>() {

    private val viewModel by viewModels<WelcomeViewModel>()

    override val backPressedHandler: () -> Unit
        get() = { requireActivity().finishAffinity() }
    override val onCreate: () -> Unit
        get() = {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentWelcomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observables()

    }

    private fun observables() {
        viewModel.appEvent.observe(viewLifecycleOwner) {
            when (it) {
                is AppEvent.NavigateFragmentEvent -> {
                    if (it.screenID == R.id.loginFragment) {
                        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
                    } else if (it.screenID == R.id.registerFragment) {
                        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment())
                    }

                }

                else -> {}
            }
            viewModel._appEvent.postValue(null)
        }
    }


}