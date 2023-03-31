package com.cl.swafoody.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class InstructionResponse(

	@field:SerializedName("InstructionResponse")
	val instructionResponse: List<InstructionResponseItem?>? = null
)

data class StepsItem(

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem?>? = null,

	@field:SerializedName("equipment")
	val equipment: List<EquipmentItem?>? = null,

	@field:SerializedName("step")
	val step: String? = null,

	@field:SerializedName("length")
	val length: Length? = null
)

data class Length(

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("unit")
	val unit: String? = null
)

data class EquipmentItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("localizedName")
	val localizedName: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class InstructionResponseItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("steps")
	val steps: List<StepsItem?>? = null
)
