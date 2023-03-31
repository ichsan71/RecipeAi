package com.cl.swafoody.ui.detail.ingredient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.response.IngredientsItem
import com.cl.swafoody.databinding.FragmentIngredientBinding
import com.cl.swafoody.ui.detail.DetailRecipeActivity
import com.cl.swafoody.ui.detail.nutritions.NutritionViewModel
import com.cl.swafoody.ui.recipe.RecipeAdapter

class IngredientFragment : Fragment() {
    private var fragmentIngredientBinding: FragmentIngredientBinding? = null
    private val binding get() = fragmentIngredientBinding
    private val viewModel: IngredientViewModel by viewModels()
    private lateinit var adapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentIngredientBinding = FragmentIngredientBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity: DetailRecipeActivity? = activity as DetailRecipeActivity?
        val recipeId: Int? = activity?.sendData()
        adapter = IngredientAdapter()

        showIngredient()
        with(binding?.rvIngredient) {
            this?.layoutManager = LinearLayoutManager(requireContext())
            this?.setHasFixedSize(true)
            this?.adapter = adapter
        }
        if (recipeId != null) {
            viewModel.getRecipeIngredient(recipeId)
        }
    }

    private fun showIngredient() {
        viewModel.dataIngredient.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Success -> {
                    binding?.rvIngredient?.adapter = adapter
                    adapter.setIngredients(it.value.ingredients!!)
                    binding?.progressBar?.visibility = View.GONE
                }
                is ResultState.Failure -> {
                    Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is ResultState.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentIngredientBinding = null
    }
}