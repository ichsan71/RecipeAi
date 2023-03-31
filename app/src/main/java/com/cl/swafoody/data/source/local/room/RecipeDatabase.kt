package com.cl.swafoody.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cl.swafoody.data.source.local.entity.RecipeEntity
import com.cl.swafoody.data.source.remote.response.RecipeItem

@Database(entities = [RecipeItem::class],
    version = 3,
    exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): SwafoodyDao

    companion object {

        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "Finme.db"
                )
                    .fallbackToDestructiveMigrationFrom(2) // Add this line to allow for destructive migrations
                    .build().apply {
                        INSTANCE = this
                    }
                }
            }
    }