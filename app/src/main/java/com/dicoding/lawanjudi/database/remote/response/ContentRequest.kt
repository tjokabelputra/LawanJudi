package com.dicoding.lawanjudi.database.remote.response

import com.google.gson.annotations.SerializedName

data class ContentRequest(
    @field:SerializedName("contents")
    val contents: List<ContentsItem>
)

data class ContentsItem(
    @field:SerializedName("parts")
    val parts: List<Parts>
)

data class Parts(
    @field:SerializedName("text")
    val text: String
)
