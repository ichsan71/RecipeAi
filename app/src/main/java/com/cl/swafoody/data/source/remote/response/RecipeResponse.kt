package com.cl.swafoody.data.source.remote.response

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class RecipeResponse(
	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("offset")
	val offset: Int? = null,

	@field:SerializedName("results")
	val results: List<RecipeItem>
)

@Entity(tableName = "recipe_entities_item")
data class RecipeItem(
	@ColumnInfo(name = "image")
	@field:SerializedName("image")
	val image: String,

	@PrimaryKey
	@ColumnInfo(name = "id")
	@field:SerializedName("id")
	val id: Int,

	@ColumnInfo(name = "title")
	@field:SerializedName("title")
	val title: String,

	@ColumnInfo(name = "imageType")
	@field:SerializedName("imageType")
	val imageType: String,

	@ColumnInfo(name = "isBookmarked")
	@field:SerializedName("isBookmarked")
	var isBookmarked: Boolean
)
