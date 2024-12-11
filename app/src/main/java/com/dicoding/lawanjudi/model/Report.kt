package com.dicoding.lawanjudi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val content : String? = null,
    val description: String? = null,
    val aiConfirmed: Boolean? = false
) : Parcelable
