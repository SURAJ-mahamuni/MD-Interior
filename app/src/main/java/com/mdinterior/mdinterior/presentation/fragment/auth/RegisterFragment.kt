package com.mdinterior.mdinterior.presentation.fragment.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentRegisterBinding
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.onTouchHideKeyboard
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.helper.Extensions.toastMsg
import com.mdinterior.mdinterior.presentation.viewModels.auth.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BindingFragment<FragmentRegisterBinding>() {

    private val viewModel by viewModels<RegisterViewModel>()

    override val backPressedHandler: () -> Unit
        get() = { findNavController().navigateUp() }
    override val onCreate: () -> Unit
        get() = {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentRegisterBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTouchHideKeyboard(view) {}

        observables()

    }

    private fun observables() {
        viewModel.appEvent.observe(viewLifecycleOwner) {
            when (it) {
                is AppEvent.Other -> {
                    if (it.message == "hide_progressBar") {
                        binding.progressBar.hideView()
                        binding.progressBar.isEnabled = true
                    } else if (it.message == "show_progressBar") {
                        binding.progressBar.isEnabled = false
                        binding.progressBar.showView()
                    }
                }

                is AppEvent.NavigateBackEvent -> {
                    findNavController().navigateUp()
                    binding.progressBar.hideView()
                }

                is AppEvent.ToastEvent -> {
                    Log.e("toast", getString(it.message))
                    toastMsg(getString(it.message))
                    binding.progressBar.hideView()
                }

                is AppEvent.ToastEventString -> {
                    toastMsg(it.message)
                    binding.progressBar.hideView()
                }

                else -> {}
            }
            viewModel._appEvent.postValue(null)

        }
    }

}