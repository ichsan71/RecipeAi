package com.cl.swafoody.data.source.remote

import com.cl.swafoody.data.source.remote.response.RecipeItem
import com.google.gson.annotations.SerializedName

data class ResponseList<T>(
    @field:SerializedName("number")
    val number: Int? = null,

    @field:SerializedName("totalResults")
    val totalResults: Int? = null,

    @field:SerializedName("offset")
    val offset: Int? = null,

    @field:SerializedName("results")
    val results: List<RecipeItem>
)