package com.mdinterior.mdinterior.presentation.fragment.client.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.databinding.FragmentHomeBinding
import com.mdinterior.mdinterior.databinding.ProjectCardItemBinding
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.AppEvent
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.helper.Extensions.toastMsg
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import com.mdinterior.mdinterior.presentation.viewModels.client.ClientHomeViewModel
import com.mdinterior.mdinterior.presentation.viewModels.client.ClientMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<ClientHomeViewModel>()

    private val mainViewModel by activityViewModels<ClientMainViewModel>()

    private var _projectAdapter: GenericAdapter<Project, ProjectCardItemBinding>? = null

    private val projectAdapter get() = _projectAdapter!!

    override val backPressedHandler: () -> Unit
        get() = {
            requireActivity().finishAffinity()
        }
    override val onCreate: () -> Unit
        get() = {
            mainViewModel.setTitle()
            viewModel.getHomeData()
            viewModel.getHomeRecentWorkData()
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

                is AppEvent.ToastEvent -> toastMsg(getString(it.message))

                else -> {}
            }
            viewModel._appEvent.postValue(null)
        }
        viewModel.homeData.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseEvents.FirebaseError -> {
                    Log.e("home", it.error)
                    binding.progressBar5.hideView()
                }

                FireBaseEvents.FirebaseLoading -> {
                    binding.progressBar5.showView()
                }

                is FireBaseEvents.FirebaseSuccess -> {
//                    val data = JsonConvertor.jsonToObject<User>(it.data)
//                    Log.e("user", data.toString())
                    binding.progressBar5.hideView()
                }
            }
        }
        viewModel.homeDataRecentWorkData.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseEvents.FirebaseError -> {
                    Log.e("home", it.error)
                    binding.progressBar5.hideView()
                }

                FireBaseEvents.FirebaseLoading -> {
                    binding.progressBar5.showView()
                }

                is FireBaseEvents.FirebaseSuccess -> {
                    val data = JsonConvertor.jsonToObject<AllProjects>(it.data)
                    Log.e("home", data.toString())
                    projectAdapter.setData(ArrayList(data.projects))
                    binding.progressBar5.hideView()
                }
            }
        }
    }

    private fun initializeAdapter() {
        _projectAdapter = GenericAdapter(
            bindingInflater = ProjectCardItemBinding::inflate,
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

}