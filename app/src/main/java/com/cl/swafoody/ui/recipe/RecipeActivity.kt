package com.cl.swafoody.ui.recipe

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cl.swafoody.R
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.databinding.ActivityRecipeBinding
import com.cl.swafoody.ui.ViewModelFactory
import com.cl.swafoody.data.source.remote.Result
import com.cl.swafoody.ui.home.HomeFragment

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter
    private var name: String? = null
    private var tabName: String? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabName = intent.getStringExtra(ARG_TAB)
        name = intent.getStringExtra(EXTRA_NAME)

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: RecipeViewModel by viewModels {
            factory
        }

        val recipeAdapter = RecipeAdapter { recipe ->
            if (recipe.isBookmarked) {
                viewModel.deleteRecipe(recipe)
            } else {
                viewModel.saveRecipe(recipe)
            }
        }

        if (tabName == TAB_RECIPE) {
            viewModel.getRecipeSaved().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            Log.d("RecipeActivity", "Loading TAB_RECIPE")
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val recipeData = result.data
                            recipeAdapter.submitList(recipeData)
                            Log.d("RecipeActivity", "Success TAB_RECIPE")
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan TAB_RECIPE" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("RecipeActivity", "Error TAB_RECIPE")
                        }
                    }
                }
            }
        } else if (tabName == TAB_BOOKMARK) {
            viewModel.getBookmarkedRecipe().observe(this) { bookmarkRecipe ->
                binding.progressBar.visibility = View.GONE
                recipeAdapter.submitList(bookmarkRecipe)
                Log.d("RecipeActivity", "Success TAB_BOOKMARK")
            }
        } else {
            Log.d("RecipeActivity", "Ga masuk ey")
            viewModel.getRecipeSaved().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            Log.d("RecipeActivity", "Loading")
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            recipeAdapter.submitList(result.data)
                            Log.d("RecipeActivity", "Success ini mah ${result.data.size}")
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("RecipeActivity", "Error")
                        }
                    }
                }
            }
        }

        viewModel.getRecipeByIngredients(name!!)

        //showRecyclerList()

        val recyclerView = binding.rvRecipe
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = recipeAdapter

        val fragment = HomeFragment()
        fragment.arguments = Bundle().apply {
            putString(HomeFragment.ARG_TAB, tabName)
        }
        supportFragmentManager.commit {
            add(R.id.container, fragment)
        }

    }

    private fun showRecyclerList() {
        binding.apply {
            viewModel.apply {
                dataRecipe.observe(this@RecipeActivity) {
                    when (it) {
                        is ResultState.Success -> {
//                            val adapter = RecipeAdapter()
//                            rvRecipe.adapter = adapter
//                            adapter.setRecipes(it.value)
                            Log.d("RISA", it.value.toString())
                            binding.progressBar.visibility = View.GONE
                        }
                        is ResultState.Failure -> {
                            Toast.makeText(this@RecipeActivity, it.throwable.message, Toast.LENGTH_LONG).show()
                        }
                        is ResultState.Loading -> {
                        }
                    }
                }
                dataFilter.observe(this@RecipeActivity) {
                    when (it) {
                        is ResultState.Success -> {
//                            val adapter = RecipeAdapter()
//                            rvRecipe.adapter = adapter
//                            adapter.setRecipes(it.value)
                            Log.d("SARI", it.value.toString())
                            binding.progressBar.visibility = View.GONE
                        }
                        is ResultState.Failure -> {
                            Toast.makeText(this@RecipeActivity, it.throwable.message, Toast.LENGTH_LONG).show()
                        }
                        is ResultState.Loading -> {
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.diet_keto-> viewModel.getRecipeByDiet(name!!, "Keto")
            R.id.diet_vegan -> viewModel.getRecipeByDiet(name!!, "Vegan")
        }
        item.isChecked = true
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val ARG_TAB = "tab_name"
        const val TAB_RECIPE = "recipe"
        const val TAB_BOOKMARK = "bookmark"
    }

}