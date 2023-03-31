package com.cl.swafoody.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RecipeInformationResponse(
	val results: List<RecipeInformationItem>
)

data class RecipeInformationItem(

	@field:SerializedName("instructions")
	val instructions: String? = null,

	@field:SerializedName("sustainable")
	val sustainable: Boolean? = null,

	@field:SerializedName("analyzedInstructions")
	val analyzedInstructions: List<AnalyzedInstructionsItem?>? = null,

	@field:SerializedName("glutenFree")
	val glutenFree: Boolean? = null,

	@field:SerializedName("veryPopular")
	val veryPopular: Boolean? = null,

	@field:SerializedName("healthScore")
	val healthScore: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("diets")
	val diets: List<String?>? = null,

	@field:SerializedName("aggregateLikes")
	val aggregateLikes: Int? = null,

	@field:SerializedName("creditsText")
	val creditsText: String? = null,

	@field:SerializedName("readyInMinutes")
	val readyInMinutes: Int? = null,

	@field:SerializedName("sourceUrl")
	val sourceUrl: String? = null,

	@field:SerializedName("dairyFree")
	val dairyFree: Boolean? = null,

	@field:SerializedName("servings")
	val servings: Int? = null,

	@field:SerializedName("vegetarian")
	val vegetarian: Boolean? = null,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("preparationMinutes")
	val preparationMinutes: Int? = null,

	@field:SerializedName("imageType")
	val imageType: String? = null,

	@field:SerializedName("winePairing")
	val winePairing: WinePairing? = null,

	@field:SerializedName("summary")
	val summary: String? = null,

	@field:SerializedName("cookingMinutes")
	val cookingMinutes: Int? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("veryHealthy")
	val veryHealthy: Boolean? = null,

	@field:SerializedName("vegan")
	val vegan: Boolean? = null,

	@field:SerializedName("cheap")
	val cheap: Boolean? = null,

	@field:SerializedName("extendedIngredients")
	val extendedIngredients: List<ExtendedIngredientsItem?>? = null,

	@field:SerializedName("dishTypes")
	val dishTypes: List<String?>? = null,

	@field:SerializedName("gaps")
	val gaps: String? = null,

	@field:SerializedName("cuisines")
	val cuisines: List<Any?>? = null,

	@field:SerializedName("lowFodmap")
	val lowFodmap: Boolean? = null,

	@field:SerializedName("license")
	val license: String? = null,

	@field:SerializedName("weightWatcherSmartPoints")
	val weightWatcherSmartPoints: Int? = null,

	@field:SerializedName("occasions")
	val occasions: List<Any?>? = null,

	@field:SerializedName("pricePerServing")
	val pricePerServing: Double? = null,

	@field:SerializedName("sourceName")
	val sourceName: String,

	@field:SerializedName("originalId")
	val originalId: Any? = null,

	@field:SerializedName("spoonacularSourceUrl")
	val spoonacularSourceUrl: String? = null
)

data class Measures(

	@field:SerializedName("metric")
	val metric: Metric? = null,

	@field:SerializedName("us")
	val us: Us? = null
)

data class AnalyzedInstructionsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("steps")
	val steps: List<StepsItem?>? = null
)

data class WinePairing(
	val any: Any? = null
)

data class ExtendedIngredientsItem(

	@field:SerializedName("originalName")
	val originalName: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("measures")
	val measures: Measures? = null,

	@field:SerializedName("nameClean")
	val nameClean: String? = null,

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("meta")
	val meta: List<Any?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("aisle")
	val aisle: String? = null,

	@field:SerializedName("consistency")
	val consistency: String? = null
)
