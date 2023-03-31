package com.cl.swafoody.ui.recipe

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cl.swafoody.R
import com.cl.swafoody.data.source.local.entity.RecipeEntity
import com.cl.swafoody.data.source.remote.response.RecipeItem
import com.cl.swafoody.databinding.ItemBinding
import com.cl.swafoody.ui.detail.DetailRecipeActivity
import com.cl.swafoody.utils.RecipeDiffCallback

class RecipeAdapter(val onBookmarkClick: (RecipeItem) -> Unit) : ListAdapter<RecipeItem, RecipeAdapter.RecipeViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemsRecipeBinding =
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemsRecipeBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)

        val ivBookmark = holder.binding.ivFavorite
        if (items.isBookmarked) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_favorite))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_favorite_border))
        }

        ivBookmark.setOnClickListener {
            onBookmarkClick(items)
        }
    }

    class RecipeViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(recipe: RecipeItem) {
            with(binding) {
                tvTitle.text = recipe.title
                Glide.with(itemView.context)
                    .load("https://spoonacular.com/recipeImages/" + recipe.id + "-312x231.jpg")
                    .transform(RoundedCorners(24))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgPoster)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailRecipeActivity::class.java)
                    intent.putExtra(DetailRecipeActivity.EXTRA_RECIPE, recipe.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<RecipeItem> =
            object : DiffUtil.ItemCallback<RecipeItem>() {
                override fun areItemsTheSame(oldUser: RecipeItem, newUser: RecipeItem): Boolean {
                    return oldUser.title == newUser.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: RecipeItem, newUser: RecipeItem): Boolean {
                    return oldUser == newUser
                }
            }
    }
}

