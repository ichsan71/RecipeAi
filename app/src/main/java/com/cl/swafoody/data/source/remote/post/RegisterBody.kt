package com.cl.swafoody.data.source.remote.post

import com.google.gson.annotations.SerializedName

data class RegisterBody (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)