package com.cl.swafoody.ui.detail.instruction

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.response.InstructionResponse
import com.cl.swafoody.databinding.FragmentInstructionBinding
import com.cl.swafoody.network.ApiConfig
import com.cl.swafoody.ui.detail.DetailRecipeActivity
import com.cl.swafoody.ui.detail.nutritions.NutritionViewModel

class InstructionFragment : Fragment() {
    private var fragmentInstructionBinding: FragmentInstructionBinding? = null
    private val binding get() = fragmentInstructionBinding
    private val viewModel: InstructionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInstructionBinding = FragmentInstructionBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity: DetailRecipeActivity? = activity as DetailRecipeActivity?
        val recipeId: Int? = activity?.sendData()

        showInstruction()
        if (recipeId != null) {
            viewModel.getRecipeInstruction(recipeId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentInstructionBinding = null
    }

    @SuppressLint("SetTextI18n")
    private fun showInstruction() {
        viewModel.dataInstruction.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Success -> {

                    binding?.step1Title?.text = "Step 1"
                    binding?.step1Description?.text = it.value.instructionResponse?.get(0)?.steps?.get(0)?.step
                    binding?.step1IngredientsTitle?.text  = "Ingredients"
                    binding?.step1IngredientsList?.text = it.value.instructionResponse?.get(0)?.steps?.get(0)?.ingredients?.get(0)?.name
                    binding?.step1EquipmentTitle?.text = "Equipment"
                    binding?.step1EquipmentList?.text = it.value.instructionResponse?.get(0)?.steps?.get(0)?.equipment?.get(0)?.name
                    binding?.step1Time?.text = it.value.instructionResponse?.get(0)?.steps?.get(0)?.length?.number.toString() + " " + it.value.instructionResponse?.get(0)?.steps?.get(0)?.length?.unit
                }
                is ResultState.Failure -> {
                    Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                    Log.d("InstructionFragment", it.throwable.message.toString())
                }
                is ResultState.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
            }
        }
    }
}