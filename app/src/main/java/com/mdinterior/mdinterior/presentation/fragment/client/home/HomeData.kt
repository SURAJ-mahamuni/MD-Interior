package com.mdinterior.mdinterior.presentation.fragment.client.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeData (
    val dashboard: Dashboard? = null,

    @SerializedName("emailId")
    val emailID: String? = null,

    val isAdmin: Boolean? = null,
    val mobileNumber: String? = null,
    val name: String? = null
) : Parcelable

@Parcelize
data class Dashboard (
    val subtitle: String? = null,
    val title: String? = null
) : Parcelable

@Parcelize
data class AllProjects (
    val projects: List<Project>? = null
) : Parcelable

@Parcelize
data class Project (
    val location: String? = null,
    val dateTime: String? = null,
    val description: String? = null,
    val projectTitle: String? = null,

    @SerializedName("relatedPhotosUrl")
    val relatedPhotosURL: List<String>? = null,

    val siteType: String? = null
) : Parcelable

