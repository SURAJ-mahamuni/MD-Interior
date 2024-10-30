package com.mdinterior.mdinterior.presentation.fragment.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.databinding.FragmentServicesBinding
import com.mdinterior.mdinterior.databinding.SerivesItemBinding
import com.mdinterior.mdinterior.presentation.model.DemoData
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : BindingFragment<FragmentServicesBinding>() {

    private var _serviceAdapter: GenericAdapter<DemoData, SerivesItemBinding>? = null

    private val serviceAdapter get() = _serviceAdapter!!

    override val backPressedHandler: () -> Unit
        get() = {}
    override val onCreate: () -> Unit
        get() = {}
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentServicesBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

    }

    private fun initializeAdapter() {
        _serviceAdapter = GenericAdapter(
            bindingInflater = SerivesItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    textView6.text = "We recently completed an elegant and functional interior design project for a cozy 1BHK apartment in the heart of Shivajinagar. Our design approach was tailored to maximize space while maintaining a modern and comfortable living environment."
                }
            })
        binding.servicesRv.adapter = serviceAdapter

        serviceAdapter.setData(ArrayList<DemoData>().apply {
            for (i in 0..10) {
                add(DemoData("$i"))
            }
        })
    }

}