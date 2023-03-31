package com.cl.swafoody.utils

import androidx.recyclerview.widget.DiffUtil
import com.cl.swafoody.data.source.remote.response.RecipeItem

class RecipeDiffCallback(private val mOldRecipeList: List<RecipeItem>, private val mNewRecipeList: List<RecipeItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldRecipeList.size
    }

    override fun getNewListSize(): Int {
        return mNewRecipeList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldRecipeList[oldItemPosition].id == mNewRecipeList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = mOldRecipeList[oldItemPosition]
        val newData = mNewRecipeList[newItemPosition]
        return oldData.title == newData.title && oldData.id == newData.id
    }
}