package com.cl.swafoody.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
