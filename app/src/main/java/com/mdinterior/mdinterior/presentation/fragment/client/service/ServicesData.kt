package com.mdinterior.mdinterior.presentation.fragment.client.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Services (
    val service: List<Service>? = null
) : Parcelable

@Parcelize
data class Service (
    val details: String? = null,
    val image: String? = null,
    val service: String? = null
) : Parcelable