package com.cl.swafoody.ui.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cl.swafoody.databinding.FragmentRecipeBinding
import com.cl.swafoody.ui.ViewModelFactory

class RecipeFragment : Fragment() {
    private var fragmentRecipeBinding: FragmentRecipeBinding? = null
    private val binding get() = fragmentRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentRecipeBinding = FragmentRecipeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (activity != null) {


            val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
            val viewModel: RecipeViewModel by viewModels {
                factory
            }

            val recipeAdapter = RecipeAdapter { recipe ->
                if (recipe.isBookmarked == true) {
                    viewModel.deleteRecipe(recipe)
                } else {
                    viewModel.saveRecipe(recipe)
                }
            }

            with(binding?.rvRecipe) {
                this?.layoutManager = LinearLayoutManager(context)
                this?.setHasFixedSize(true)
                this?.adapter = recipeAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentRecipeBinding = null
    }
}