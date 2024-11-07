package com.mdinterior.mdinterior.presentation.fragment.startScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentSplashScreenBinding
import com.mdinterior.mdinterior.presentation.activity.AdminActivity
import com.mdinterior.mdinterior.presentation.activity.ClientActivity
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.viewModels.startScreen.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : BindingFragment<FragmentSplashScreenBinding>() {

    private val viewModel by viewModels<SplashScreenViewModel>()

    override val backPressedHandler: () -> Unit
        get() = {}
    override val onCreate: () -> Unit
        get() = {
            viewModel.checkNavigation()
        }
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSplashScreenBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observables()

    }

    private fun observables() {
        viewModel.appEvent.observe(viewLifecycleOwner) {
            when (it) {
                is AppEvent.NavigateActivityEvent -> {
                    when (it.screenID) {
                        "Admin" -> {
                            val intent = Intent(requireActivity(), AdminActivity::class.java)
                            startActivity(intent)
                        }

                        "Client" -> {
                            val intent = Intent(requireActivity(), ClientActivity::class.java)
                            startActivity(intent)
                        }

                        "Welcome" -> {
                            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToWelcomeFragment())
                        }

                        else -> {}
                    }
                }

                else -> {}
            }
        }
    }

}