package com.mdinterior.mdinterior.presentation.fragment.client.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentServicesBinding
import com.mdinterior.mdinterior.databinding.SerivesItemBinding
import com.mdinterior.mdinterior.domain.firebase.FireBaseEvents
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.helper.Base64Convertor
import com.mdinterior.mdinterior.presentation.helper.Extensions.hideView
import com.mdinterior.mdinterior.presentation.helper.Extensions.showView
import com.mdinterior.mdinterior.presentation.helper.JsonConvertor
import com.mdinterior.mdinterior.presentation.viewModels.client.ClientServicesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : BindingFragment<FragmentServicesBinding>() {

    private var _serviceAdapter: GenericAdapter<Service, SerivesItemBinding>? = null

    private val serviceAdapter get() = _serviceAdapter!!

    private val viewModel by viewModels<ClientServicesViewModel>()

    override val backPressedHandler: () -> Unit
        get() = {}
    override val onCreate: () -> Unit
        get() = {
            viewModel.getHomeRecentWorkData()
        }
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentServicesBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

        observables()

    }

    private fun observables() {
        viewModel.servicesData.observe(viewLifecycleOwner) {
            when (it) {
                is FireBaseEvents.FirebaseError -> {
                    Log.e("home", it.error)
                    binding.progressbar.hideView()
                }

                FireBaseEvents.FirebaseLoading -> {
                    binding.progressbar.showView()
                }

                is FireBaseEvents.FirebaseSuccess -> {
                    val data = JsonConvertor.jsonToObject<Services>(it.data)
                    Log.e("home", data.toString())
                    serviceAdapter.setData(ArrayList(data.service))
                    binding.progressbar.hideView()
                }
            }
        }
    }

    private fun initializeAdapter() {
        _serviceAdapter = GenericAdapter(
            bindingInflater = SerivesItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    serviceName.text = itemData.service
                    serviceDetails.text = itemData.details
//                    imageView2.setImageBitmap(Base64Convertor.convertToBitmap(itemData.image ?: ""))
                }
            })
        binding.servicesRv.adapter = serviceAdapter
    }

}