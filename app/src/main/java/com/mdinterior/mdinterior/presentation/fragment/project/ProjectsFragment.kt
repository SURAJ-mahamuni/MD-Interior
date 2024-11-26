package com.mdinterior.mdinterior.presentation.fragment.project

import android.os.Bundle
import android.util.ArraySet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.chip.Chip
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.databinding.FragmentProjectsBinding
import com.mdinterior.mdinterior.databinding.ProjectCardItemBinding
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.model.DemoData
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.fragment.client.home.AllProjects
import com.mdinterior.mdinterior.presentation.fragment.client.home.Project
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import com.mdinterior.mdinterior.presentation.viewModels.admin.AdminMainViewModel
import com.mdinterior.mdinterior.presentation.viewModels.client.ClientMainViewModel
import com.mdinterior.mdinterior.presentation.viewModels.client.ClientProjectViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Random

@AndroidEntryPoint
class ProjectsFragment : BindingFragment<FragmentProjectsBinding>() {

    private val clientMainViewModel by activityViewModels<ClientMainViewModel>()
    private val adminMainViewModel by activityViewModels<AdminMainViewModel>()

    private var _projectAdapter: GenericAdapter<Project, ProjectCardItemBinding>? = null

    private val projectAdapter get() = _projectAdapter!!

    private var projectList = ArrayList<Project>()

    private val viewModel by viewModels<ClientProjectViewModel>()

    private var siteType = ArraySet<String>()

    override val backPressedHandler: () -> Unit
        get() = {
            findNavController().navigateUp()
        }
    override val onCreate: () -> Unit
        get() = {
            viewModel.getProjectsData()
        }
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
        adminMainViewModel.appEvent.observe(viewLifecycleOwner) {
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
            adminMainViewModel._appEvent.postValue(null)
        }
        viewModel.projectsData.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseEvents.FirebaseError -> {
                    Log.e("home", it.error)
                    binding.progressBar.hideView()
                }

                FireBaseEvents.FirebaseLoading -> {
                    binding.progressBar.showView()
                }

                is FireBaseEvents.FirebaseSuccess -> {
                    val data = JsonConvertor.jsonToObject<AllProjects>(it.data)
                    Log.e("home", data.toString())
                    projectList =
                        ArrayList(data.projects)
                    projectAdapter.setData(projectList)
                    initializeChips(ArrayList(projectList.map { it.siteType ?: "" }
                        .filter { it != "" }))
                    binding.progressBar.hideView()
                }
            }
        }
    }

    private fun initializeAdapter() {
        _projectAdapter = GenericAdapter(bindingInflater = ProjectCardItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    typeOfSite.text = itemData.siteType
                    dateTime.text = dateFormat(itemData.dateTime ?: "")
                    projectTitle.text = getString(
                        R.string.title_and_location,
                        itemData.projectTitle,
                        itemData.location
                    )
                    projectDescription.text = itemData.description
                }
            })
        binding.projectRv.adapter = projectAdapter
    }

    private fun dateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initializeChips(siteListType: ArrayList<String>) {
        binding.chipGroup.removeAllViewsInLayout()
        siteType = ArraySet<String>()
        siteListType.add(0, "All")
        siteType = ArraySet<String>().apply {
            siteListType.forEach {
                try {
                    add(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        siteListType.let {
            siteType.sortedBy { it != "All" }.forEachIndexed { index, _divisionsChip ->
                val chip = LayoutInflater.from(requireContext())
                    .inflate(R.layout.division_chip_item, binding.chipGroup, false) as Chip
                chip.id = Random().nextInt()
                chip.text = _divisionsChip
                if (index == 0) {
                    chip.isChecked = true
                    projectAdapter.setData(projectList)
                }
                if (index == siteType.size) {
                    val params = chip.layoutParams as ViewGroup.MarginLayoutParams
                    params.marginEnd = 24
                }
                if (_divisionsChip != null) binding.chipGroup.addView(chip)
            }
        }
        initializeChipListener()
    }

    private fun initializeChipListener() {
        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedChip = view?.findViewById<Chip>(checkedIds.first())
                selectedChip?.let { chip ->
                    val filterData = if (chip.text == "All") {
                        projectList
                    } else {
                        projectList.filter { it.siteType == chip.text }
                    }
                    projectAdapter.setData(ArrayList(filterData))
                }
            }
        }
    }

}