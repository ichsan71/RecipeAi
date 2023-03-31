package com.cl.swafoody.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cl.swafoody.data.source.remote.response.RecipeItem

@Dao
interface SwafoodyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(users: List<RecipeItem>)

    @Query("SELECT * FROM recipe_entities_item")
    fun getAllRecipe(): LiveData<List<RecipeItem>>

    @Query("SELECT * FROM recipe_entities_item where isBookmarked = 1")
    fun getBookmarkedRecipe(): LiveData<List<RecipeItem>>

    @Update
    suspend fun updateRecipe(recipe: RecipeItem)

    @Query("DELETE FROM recipe_entities_item WHERE isBookmarked = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM recipe_entities_item WHERE title = :title AND isBookmarked = 1)")
    suspend fun isRecipeBookmarked(title: String): Boolean
}
