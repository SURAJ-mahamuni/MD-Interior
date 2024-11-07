package com.mdinterior.mdinterior.presentation.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentLoginBinding
import com.mdinterior.mdinterior.presentation.activity.AdminActivity
import com.mdinterior.mdinterior.presentation.activity.ClientActivity
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.onTouchHideKeyboard
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.helper.Extensions.toastMsg
import com.mdinterior.mdinterior.presentation.viewModels.auth.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel>()

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
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTouchHideKeyboard(view) {}

        observables()

    }

    private fun observables() {
        viewModel.appEvent.observe(viewLifecycleOwner) {
            when (it) {
                is AppEvent.NavigateBackEvent -> {
                    findNavController().navigateUp()
                }

                is AppEvent.Other -> {
                    if (it.message == "hide_progressBar") {
                        binding.progressBar2.hideView()
                        binding.progressBar2.isEnabled = false
                    } else if (it.message == "show_progressBar") {
                        binding.progressBar2.isEnabled = true
                        binding.progressBar2.showView()
                    }
                    viewModel._appEvent.postValue(null)
                }

                is AppEvent.NavigateFragmentEvent -> {
                    if (it.screenID == 0) {
                        val intent = Intent(requireActivity(), ClientActivity::class.java)
                        startActivity(intent)
                        viewModel.setLoggedIn()
                    } else if (it.screenID == 1) {
                        val intent = Intent(requireActivity(), AdminActivity::class.java)
                        startActivity(intent)
                        viewModel.setLoggedIn()
                    }
                }

                is AppEvent.ToastEvent -> {
                    Log.e("toast", getString(it.message))
                    toastMsg(getString(it.message))
                    viewModel._appEvent.postValue(null)
                }

                else -> {}
            }
            viewModel._appEvent.postValue(null)
        }
    }

}