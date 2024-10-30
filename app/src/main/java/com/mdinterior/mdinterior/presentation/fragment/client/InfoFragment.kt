package com.mdinterior.mdinterior.presentation.fragment.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mdinterior.mdinterior.R
import com.mdinterior.mdinterior.databinding.FragmentInfoBinding
import com.mdinterior.mdinterior.databinding.ReviewItemBinding
import com.mdinterior.mdinterior.presentation.adapter.GenericAdapter
import com.mdinterior.mdinterior.presentation.fragment.BindingFragment
import com.mdinterior.mdinterior.presentation.model.DemoData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BindingFragment<FragmentInfoBinding>() {

    private var _reviewAdapter: GenericAdapter<DemoData, ReviewItemBinding>? = null

    private val reviewAdapter get() = _reviewAdapter!!

    override val backPressedHandler: () -> Unit
        get() = {}
    override val onCreate: () -> Unit
        get() = {}
    override val onDestroyViewHandler: () -> Unit
        get() = {}
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentInfoBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()

    }

    private fun initializeAdapter() {
        _reviewAdapter = GenericAdapter(
            bindingInflater = ReviewItemBinding::inflate,
            onBind = { itemData, itemBinding, position, listSize ->
                itemBinding.apply {
                    rating.setRating(itemData.demo.toInt())
                    rating.setRatingEnabled(enabled = false)
                }
            })
        binding.reviewRv.adapter = reviewAdapter

        val list = ArrayList<DemoData>().apply {
            for (i in 0..10) {
                add(DemoData(i.toString()))
            }
        }
        reviewAdapter.setData(list)

    }

}