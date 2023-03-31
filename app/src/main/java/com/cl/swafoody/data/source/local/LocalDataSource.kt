package com.cl.swafoody.data.source.local

import com.cl.swafoody.data.source.local.entity.RecipeEntity
import com.cl.swafoody.data.source.local.room.SwafoodyDao
import com.cl.swafoody.data.source.remote.response.RecipeItem

class LocalDataSource private constructor(private val dao: SwafoodyDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(swafoodyDao: SwafoodyDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(swafoodyDao)
    }

    suspend fun insertRecipe(recipe: List<RecipeItem>) = dao.insertRecipe(recipe)
    suspend fun getAllRecipe() = dao.getAllRecipe()
}