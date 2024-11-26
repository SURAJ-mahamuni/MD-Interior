package com.mdinterior.mdinterior.presentation.fragment.admin.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentAdminHomeBinding
import com.mdinterior.mdinterior.databinding.UserItemBinding
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.fragment.client.home.HomeData
import com.mdinterior.mdinterior.presentation.fragment.client.home.User
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import com.mdinterior.mdinterior.presentation.model.DemoData
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminHomeViewModel
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminHomeFragment : BindingFragment<FragmentAdminHomeBinding>() {

    private var _userAdapter: GenericAdapter<User, UserItemBinding>? = null

    private val userAdapter get() = _userAdapter!!

    private val mainViewModel by activityViewModels<AdminMainViewModel>()

    private val viewModel by viewModels<AdminHomeViewModel>()

    override val backPressedHandler: () -> Unit
        get() = {}
    override val onCreate: () -> Unit
        get() = {
            mainViewModel.setTitle()
            viewModel.getUserList()
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
            viewModel._appEvent.postValue(null)
        }
        viewModel.userList.observe(viewLifecycleOwner){
            when(it){
                is FireBaseEvents.FirebaseError -> {
                    Log.e("home", it.error)
                    binding.progressBar4.hideView()
                }

                FireBaseEvents.FirebaseLoading -> {
                    binding.progressBar4.showView()
                }

                is FireBaseEvents.FirebaseSuccess -> {
                    val data = JsonConvertor.jsonToObject<HomeData>(it.data)
                    Log.e("user", data.toString())
                    userAdapter.setData(ArrayList(data.user))
                    binding.progressBar4.hideView()
                }
            }

        }
    }

    private fun initializeAdapter() {
        _userAdapter = GenericAdapter(
            bindingInflater = UserItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    name.text = itemData.name
                    email.text = itemData.emailId
                }
            })
        binding.recentlyAddedUsersRv.adapter = userAdapter

    }

}
