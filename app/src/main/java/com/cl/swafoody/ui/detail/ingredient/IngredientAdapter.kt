package com.cl.swafoody.ui.detail.ingredient

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cl.swafoody.data.source.remote.response.IngredientsItem
import com.cl.swafoody.databinding.ItemIngredientBinding

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {
    private val listIngredients: ArrayList<IngredientsItem> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setIngredients(data: List<IngredientsItem>) {
        this.listIngredients.clear()
        this.listIngredients.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemsIngredientBinding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(itemsIngredientBinding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val items = listIngredients[position]
        holder.bind(items)
    }

    override fun getItemCount(): Int {
        return listIngredients.size
    }

    class IngredientViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: IngredientsItem) {
            with(binding) {
                tvName.text = data.name
                tvSize.text = data.amount?.metric?.value.toString()
                tvUnit.text = data.amount?.metric?.unit.toString()
            }
        }
    }
}