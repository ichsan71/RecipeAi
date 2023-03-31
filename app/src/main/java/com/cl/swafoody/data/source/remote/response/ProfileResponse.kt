package com.cl.swafoody.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
	val results: List<ProfileResponseItem>
)

data class ProfileResponseItem(
	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,
)
