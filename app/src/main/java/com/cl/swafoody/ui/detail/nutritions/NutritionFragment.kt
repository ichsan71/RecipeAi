package com.cl.swafoody.ui.detail.nutritions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.databinding.FragmentNutritionBinding
import com.cl.swafoody.ui.detail.DetailRecipeActivity

class NutritionFragment : Fragment() {
    private var fragmentNutritionBinding: FragmentNutritionBinding? = null
    private val binding get() = fragmentNutritionBinding
    private val viewModel: NutritionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNutritionBinding = FragmentNutritionBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity: DetailRecipeActivity? = activity as DetailRecipeActivity?
        val recipeId: Int? = activity?.sendData()

        showNutrition()
        if (recipeId != null) {
            viewModel.getRecipeNutrition(recipeId)
        }
    }

    private fun showNutrition() {
        viewModel.dataNutrition.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Success -> {
                    binding?.tvProteinSize?.text = it.value.protein
                    binding?.tvCarbohydratesSize?.text = it.value.carbs
                    binding?.tvCalSize?.text = it.value.calories
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
        fragmentNutritionBinding = null
    }
}