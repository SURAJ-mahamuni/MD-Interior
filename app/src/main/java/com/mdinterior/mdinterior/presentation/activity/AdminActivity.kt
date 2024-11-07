package com.mdinterior.mdinterior.presentation.activity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.databinding.ActivityAdminBinding
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminHomeViewModel
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : ActivityBinder<ActivityAdminBinding>() {

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel by viewModels<AdminMainViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityAdminBinding::inflate
    override val onCreateHandler: () -> Unit
        get() = {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            initializeNavController()

            fragmentDestinationHandle()

            observables()

            setUpUI()

        }

    private fun observables() {
        viewModel.appEvent.observe(this) {
            when (it) {
                is AppEvent.Other -> {
                    setTitle(it.message)
                }

                else -> {}
            }
        }
    }

    private fun setUpUI() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowInsetsControllerCompat(window, binding.root).isAppearanceLightStatusBars = false
    }

    private fun setTitle(title: String) {
        binding.topBar.title = "Hello $title,"
    }

    private fun fragmentDestinationHandle() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.admin_home_menu -> {}
            }
        }
    }

    private fun initializeNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.admin_home_menu,
                R.id.admin_users_menu,
                R.id.admin_projects_menu
            )
        )
        setSupportActionBar(binding.topBar)
        binding.topBar.setupWithNavController(navController, appBarConfiguration)

    }
}