package com.cl.swafoody.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cl.swafoody.R
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.databinding.ActivityDetailRecipeBinding
import com.cl.swafoody.ui.detail.ingredient.IngredientFragment
import com.cl.swafoody.ui.detail.instruction.InstructionFragment
import com.cl.swafoody.ui.detail.nutritions.NutritionFragment

class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private val viewModel: DetailRecipeViewModel by viewModels()
    private var recipeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(NutritionFragment(),"Nutritions")
        adapter.addFragment(IngredientFragment(),"Ingredients")
        adapter.addFragment(InstructionFragment(), "Instruction")
        binding.viewPager.adapter=adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        val extras = intent.extras

        recipeId = extras?.getInt(EXTRA_RECIPE)!!
        viewModel.getRecipeInformation(extras.getInt(EXTRA_RECIPE))
        showRecipe()
    }

    private fun showRecipe() {
        binding.apply {
            viewModel.apply {
                dataRecipe.observe(this@DetailRecipeActivity) {
                    when (it) {
                        is ResultState.Success -> {
                            val data = it.value
                            binding.progressBar.visibility = View.GONE
                            binding.tvTitle.text = data.title
                            Glide.with(this@DetailRecipeActivity)
                                .load("https://spoonacular.com/recipeImages/" + data.id + "-312x231.jpg")
                                .transform(RoundedCorners(24))
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                                        .error(R.drawable.ic_error)
                                )
                                .into(binding.ivRecipe)
                        }

                        is ResultState.Failure -> {
                            Toast.makeText(this@DetailRecipeActivity, it.throwable.message, Toast.LENGTH_LONG).show()
                        }
                        is ResultState.Loading -> {
                        }
                    }
                }
            }
        }
    }

    fun sendData(): Int? {
        return recipeId
    }

    companion object {
        const val EXTRA_RECIPE = "extra_recipe"
    }
}
