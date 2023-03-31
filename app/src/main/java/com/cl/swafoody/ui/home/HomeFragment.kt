package com.cl.swafoody.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cl.swafoody.R
import com.cl.swafoody.ui.allRecipe.AllRecipeActivity
import com.cl.swafoody.databinding.FragmentHomeBinding
import com.cl.swafoody.ui.ViewModelFactory
import com.cl.swafoody.ui.ingredients.IngredientsActivity
import com.cl.swafoody.ui.recipe.RecipeAdapter
import com.cl.swafoody.ui.recipe.RecipeViewModel
import com.cl.swafoody.data.source.remote.Result

class HomeFragment : Fragment() {
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding
    private var tabName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        tabName = arguments?.getString(ARG_TAB)

        if (activity != null) {
            val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
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

            if (tabName == TAB_BOOKMARK) {
                viewModel.getBookmarkedRecipe().observe(this) { bookmarkedNews ->
                    binding?.progressBar?.visibility = View.GONE
                    recipeAdapter.submitList(bookmarkedNews)
                }
            } else {
                Log.d("HomeFragment", "Ga masuk ey")
                viewModel.getRecipeSaved().observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                                Log.d("HomeFragment", "Loading")
                            }
                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                recipeAdapter.submitList(result.data)
                                Log.d("HomeFragment", "Success ini mah ${result.data.size}")
                            }
                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "Terjadi kesalahan" + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("HomeFragment", "Error")
                            }
                        }
                    }
                }
            }

            binding?.btnAddIngredients?.setOnClickListener {
                val intent = Intent(context, IngredientsActivity::class.java)
                startActivity(intent)
            }

            binding?.btnViewAll?.setOnClickListener {
                val intent = Intent(context, AllRecipeActivity::class.java)
                startActivity(intent)
            }

            with(binding?.rvRecipe) {
                this?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                this?.setHasFixedSize(true)
                this?.adapter = recipeAdapter
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHomeBinding = null
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val ARG_TAB = "tab_name"
        const val TAB_RECIPE = "recipe"
        const val TAB_BOOKMARK = "bookmark"
    }
}