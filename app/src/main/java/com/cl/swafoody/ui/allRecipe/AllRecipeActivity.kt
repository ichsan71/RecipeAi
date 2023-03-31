package com.cl.swafoody.ui.allRecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cl.swafoody.R
import com.cl.swafoody.databinding.ActivityAllRecipeBinding

class AllRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllRecipeBinding
    private lateinit var rvAllRecipe: RecyclerView

    private fun init() {
        rvAllRecipe = findViewById(R.id.rv_recipe)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        showRecyclerGrid()
    }

    private fun showRecyclerGrid() {
        with(binding) {
            rvAllRecipe.layoutManager = GridLayoutManager(this@AllRecipeActivity, 2)
            val gridAllRecipeAdapter = GridAllRecipeAdapter()
            rvAllRecipe.adapter = gridAllRecipeAdapter
        }

    }

}