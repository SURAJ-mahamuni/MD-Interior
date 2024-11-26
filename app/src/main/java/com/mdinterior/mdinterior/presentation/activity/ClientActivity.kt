package com.mdinterior.mdinterior.presentation.activity

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.databinding.ActivityClientBinding
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Constants.FILTER
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.viewModels.client.ClientMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientActivity : ActivityBinder<ActivityClientBinding>() {

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel by viewModels<ClientMainViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityClientBinding::inflate
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

    private fun setUpUI() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowInsetsControllerCompat(window, binding.root).isAppearanceLightStatusBars = false
    }

    private fun observables() {
        viewModel.appEvent.observe(this) {
            when (it) {
                is AppEvent.Other -> {
                    if (it.message != FILTER)
                        setTitle(it.message)
                }

                else -> {}
            }
        }
    }

    private fun setTitle(title: String) {
        binding.topBar.title = "Hello $title,"
    }

    private fun fragmentDestinationHandle() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment -> {
                    binding.topBar.menu.findItem(R.id.menu_filter).isVisible = false
                    binding.topBarLayout.hideView()
                    binding.bottomNavigationView.hideView()
                }

                R.id.home_menu -> {
                    binding.topBar.menu.findItem(R.id.menu_filter).isVisible = false
                    binding.topBarLayout.showView()
                    binding.bottomNavigationView.showView()
                }

                R.id.services_menu -> {
                    binding.topBar.menu.findItem(R.id.menu_filter).isVisible = false
                    binding.topBarLayout.showView()
                    binding.bottomNavigationView.showView()
                }
                R.id.info_menu -> {
                    binding.topBar.menu.findItem(R.id.menu_filter).isVisible = false
                    binding.topBarLayout.showView()
                    binding.bottomNavigationView.showView()
                }

                R.id.projectsFragment -> {
                    binding.topBar.menu.findItem(R.id.menu_filter).isVisible = true
                    binding.topBarLayout.showView()
                    binding.bottomNavigationView.hideView()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                viewModel._appEvent.postValue(AppEvent.Other(FILTER))
                true
            }

            else -> false
        }
    }

    private fun initializeNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_menu,
                R.id.services_menu,
                R.id.info_menu
            )
        )
        setSupportActionBar(binding.topBar)
        binding.topBar.setupWithNavController(navController, appBarConfiguration)

    }

}