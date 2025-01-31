package com.mdinterior.mdinterior.presentation.fragment.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentAdminUsersBinding
import com.mdinterior.mdinterior.databinding.UserDetailsDialogBinding
import com.mdinterior.mdinterior.databinding.UserItemBinding
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.fragment.client.home.HomeData
import com.mdinterior.mdinterior.presentation.fragment.client.home.User
import com.mdinterior.mdinterior.presentation.helper.Constants.CLIENT_YES
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.showCustomDialog
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminUsersFragment : BindingFragment<FragmentAdminUsersBinding>() {

    private var _userAdapter: GenericAdapter<User, UserItemBinding>? = null

    private val userAdapter get() = _userAdapter!!

    private val viewModel by viewModels<AdminUserViewModel>()

    override val backPressedHandler: () -> Unit
        get() = {}
    override val onCreate: () -> Unit
        get() = {
            viewModel.getUserList()
        }
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentAdminUsersBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

        observables()

    }

    private fun observables() {
        viewModel.userList.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseEvents.FirebaseError -> {
                    Log.e("home", it.error)
                    binding.progressBar.hideView()
                }

                FireBaseEvents.FirebaseLoading -> {
                    binding.progressBar.showView()
                }

                is FireBaseEvents.FirebaseSuccess -> {
                    val data = JsonConvertor.jsonToObject<HomeData>(it.data)
                    Log.e("user", data.toString())
                    userAdapter.setData(ArrayList(data.user?.filter { it.client == CLIENT_YES }))
                    binding.progressBar.hideView()
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
                    container.setOnClickListener {
                        openUserDetailsDialog(itemData)
                    }
                }
            })
        binding.userRv.adapter = userAdapter
    }

    private fun openUserDetailsDialog(user: User) {
        showCustomDialog(
            bindingInflater = UserDetailsDialogBinding::inflate,
            onBind = { dialogBinding, dialog ->
                dialogBinding.apply {
                    userName.text = user.name
                    userNumber.text = user.mobileNumber
                    userEmail.text = user.emailId
                }
            })
    }
}