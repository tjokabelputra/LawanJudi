package com.dicoding.lawanjudi.database.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdsPredictReponse(

	@field:SerializedName("probability")
	val probability: Double,

	@field:SerializedName("is_judi_online")
	val isJudiOnline: Boolean,

	@field:SerializedName("text")
	val text: String
) : Parcelable
