package com.cl.swafoody.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class IngredientResponse(

	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem>? = null
)

data class Us(

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("value")
	val value: Float? = null
)

data class IngredientsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amount")
	val amount: Amount? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class Amount(

	@field:SerializedName("metric")
	val metric: Metric? = null,

	@field:SerializedName("us")
	val us: Us? = null
)

data class Metric(

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("value")
	val value: Float? = null
)
