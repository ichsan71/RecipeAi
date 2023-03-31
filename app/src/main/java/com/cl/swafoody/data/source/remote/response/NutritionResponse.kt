package com.cl.swafoody.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class NutritionResponse(

	@field:SerializedName("expires")
	val expires: Long? = null,

	@field:SerializedName("bad")
	val bad: List<BadItem?>? = null,

	@field:SerializedName("carbs")
	val carbs: String? = null,

	@field:SerializedName("protein")
	val protein: String? = null,

	@field:SerializedName("fat")
	val fat: String? = null,

	@field:SerializedName("calories")
	val calories: String? = null,

	@field:SerializedName("isStale")
	val isStale: Boolean? = null,

	@field:SerializedName("good")
	val good: List<GoodItem?>? = null
)

data class GoodItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("percentOfDailyNeeds")
	val percentOfDailyNeeds: Double? = null,

	@field:SerializedName("indented")
	val indented: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class BadItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("percentOfDailyNeeds")
	val percentOfDailyNeeds: Double? = null,

	@field:SerializedName("indented")
	val indented: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null
)
