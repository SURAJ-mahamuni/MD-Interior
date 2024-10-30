package com.mdinterior.mdinterior.presentation.fragment.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentHomeBinding
import com.mdinterior.mdinterior.databinding.ProjectCardItemBinding
import com.mdinterior.mdinterior.presentation.model.DemoData
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.viewModels.ClientHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<ClientHomeViewModel>()

    private var _projectAdapter: GenericAdapter<DemoData, ProjectCardItemBinding>? = null

    private val projectAdapter get() = _projectAdapter!!

    override val backPressedHandler: () -> Unit
        get() = {
            requireActivity().finishAffinity()
        }
    override val onCreate: () -> Unit
        get() = {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

        observables()

    }

    private fun observables() {
        viewModel.appEvent.observe(viewLifecycleOwner) {
            when (it) {
                is AppEvent.NavigateFragmentEvent -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeMenuToProjectsFragment())
                }
                else -> {}
            }
            viewModel._appEvent.postValue(null)
        }
    }

    private fun initializeAdapter() {
        _projectAdapter = GenericAdapter(
            bindingInflater = ProjectCardItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    typeOfSite.text = "${itemData.demo}BHK"
                }
            })
        binding.projectRv.adapter = projectAdapter

        projectAdapter.setData(ArrayList<DemoData>().apply {
            for (i in 0..10) {
                add(DemoData("$i"))
            }
        })
    }

}