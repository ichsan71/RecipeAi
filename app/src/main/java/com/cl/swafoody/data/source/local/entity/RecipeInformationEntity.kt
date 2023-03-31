package com.cl.swafoody.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_information_entities")
data class RecipeInformationEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipeId")
    var recipeId: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "summary")
    var summary: String,

    @ColumnInfo(name = "vegetarian")
    var vegetarian: Boolean,

    @ColumnInfo(name = "instructions")
    var instructions: String,
)