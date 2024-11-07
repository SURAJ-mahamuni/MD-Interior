package com.mdinterior.mdinterior.presentation.fragment.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.firebase.firestore.auth.User
import com.mdinterior.mdinterior.databinding.FragmentAdminHomeBinding
import com.mdinterior.mdinterior.databinding.UserItemBinding
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.model.DemoData
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminHomeViewModel
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminHomeFragment : BindingFragment<FragmentAdminHomeBinding>() {

    private var _userAdapter: GenericAdapter<DemoData, UserItemBinding>? = null

    private val userAdapter get() = _userAdapter!!

    private val mainViewModel by activityViewModels<AdminMainViewModel>()

    private val viewModel by viewModels<AdminHomeViewModel>()

    override val backPressedHandler: () -> Unit
        get() = {}
    override val onCreate: () -> Unit
        get() = {
            mainViewModel.setTitle()
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAdminHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

        observables()

    }

    private fun observables() {
        viewModel.appEvent.observe(viewLifecycleOwner) {
            when (it) {
                is AppEvent.NavigateFragmentEvent -> {
                    findNavController().navigate(AdminHomeFragmentDirections.actionAdminHomeMenuToAddProjectFragment())
                }

                else -> {}
            }
        }
    }

    private fun initializeAdapter() {
        _userAdapter = GenericAdapter(
            bindingInflater = UserItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    name.text = "Jane Cooper ${itemData.demo}"
                }
            })
        binding.recentlyAddedUsersRv.adapter = userAdapter

        val list = ArrayList<DemoData>().apply {
            for (i in 0..10) {
                add(DemoData("$i"))
            }
        }

        userAdapter.setData(list)

    }

}
