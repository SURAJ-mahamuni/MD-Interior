package com.mdinterior.mdinterior.presentation.fragment.client

import android.os.Bundle
import android.util.ArraySet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.chip.Chip
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.databinding.FragmentProjectsBinding
import com.mdinterior.mdinterior.databinding.ProjectCardItemBinding
import com.mdinterior.mdinterior.presentation.model.DemoData
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.viewModels.ClientMainViewModel
import java.util.Random


class ProjectsFragment : BindingFragment<FragmentProjectsBinding>() {

    private val clientMainViewModel by activityViewModels<ClientMainViewModel>()

    private var _projectAdapter: GenericAdapter<DemoData, ProjectCardItemBinding>? = null

    private val projectAdapter get() = _projectAdapter!!

    private var siteType = ArraySet<String>()

    override val backPressedHandler: () -> Unit
        get() = {
            findNavController().navigateUp()
        }
    override val onCreate: () -> Unit
        get() = {}
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProjectsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

        observables()

    }

    private fun observables() {
        clientMainViewModel.appEvent.observe(viewLifecycleOwner) {
            when (it) {
                is AppEvent.Other -> {
                    if (binding.chips.isVisible) {
                        binding.chips.hideView()
                    } else {
                        binding.chips.showView()
                    }
                }

                else -> {}
            }
            clientMainViewModel._appEvent.postValue(null)
        }
    }

    private fun initializeAdapter() {
        _projectAdapter = GenericAdapter(bindingInflater = ProjectCardItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    typeOfSite.text = "${itemData.demo}BHK"
                }
            })
        binding.projectRv.adapter = projectAdapter

        val list = ArrayList<DemoData>().apply {
            for (i in 0..10) {
                add(DemoData("$i"))
            }
        }
        projectAdapter.setData(list)
        initializeChips(list)
    }

    private fun initializeChips(siteListType: ArrayList<DemoData>) {
        siteType = ArraySet<String>()
        siteType = ArraySet<String>().apply {
            siteListType.forEach {
                try {
                    add(it.demo + " BHK")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        siteListType.let {
            siteType.sortedBy { it != "0 BHK" }.forEachIndexed { index, _divisionsChip ->
                val chip = LayoutInflater.from(requireContext())
                    .inflate(R.layout.division_chip_item, binding.chipGroup, false) as Chip
                chip.id = Random().nextInt()
                chip.text = _divisionsChip
                if (index == siteType.size) {
                    val params = chip.layoutParams as ViewGroup.MarginLayoutParams
                    params.marginEnd = 24
                }
                if (_divisionsChip != null) binding.chipGroup.addView(chip)
            }

        }
    }

}