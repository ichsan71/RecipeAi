package com.cl.swafoody.network

import com.cl.swafoody.data.source.remote.ResponseList
import com.cl.swafoody.data.source.remote.ResponseObject
import com.cl.swafoody.data.source.remote.post.LoginBody
import com.cl.swafoody.data.source.remote.post.RegisterBody
import com.cl.swafoody.data.source.remote.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("recipes/complexSearch?apiKey=45609724949b4a5e8a84a5633b2c075d")
    suspend fun getRecipeByIngredients(
        @Query("includeIngredients") ingredients: String = ""
    ): ResponseList<RecipeItem>

    @GET("recipes/complexSearch?apiKey=45609724949b4a5e8a84a5633b2c075d")
    suspend fun getRecipeByDiet(
        @Query("includeIngredients") ingredients: String = "",
        @Query("diet") diet: String = ""
    ): ResponseList<RecipeItem>

    @GET("recipes/{id}/information?apiKey=45609724949b4a5e8a84a5633b2c075d")
    suspend fun getRecipeInformation(
        @Path("id") id: Int
    ): RecipeInformationItem

    @GET("recipes/{id}/nutritionWidget.json?apiKey=45609724949b4a5e8a84a5633b2c075d")
    suspend fun getRecipeNutrition(
        @Path("id") id: Int
    ): NutritionResponse

    @GET("recipes/{id}/ingredientWidget.json?apiKey=45609724949b4a5e8a84a5633b2c075d")
    suspend fun getRecipeIngredient(
        @Path("id") id: Int
    ): IngredientResponse

    @GET("recipes/{id}/analyzedInstructions?apiKey=45609724949b4a5e8a84a5633b2c075d")
    suspend fun getRecipeInstruction(
        @Path("id") id: Int
    ): InstructionResponse

    @POST("/auth/login")
    suspend fun postLogin(
        @Body loginBody: LoginBody
    ): ResponseObject<LoginResponse>

    @GET("/auth/me")
    suspend fun me(): ResponseObject<ProfileResponseItem>

    @POST("/users")
    suspend fun signup(
        @Body body: RegisterBody
    ): ResponseObject<RegisterResponse>
}