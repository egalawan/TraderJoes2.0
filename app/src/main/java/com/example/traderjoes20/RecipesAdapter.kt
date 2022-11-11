package com.example.traderjoes20

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.Models.Recipe
import com.example.traderjoes20.databinding.RecipeBinding

class RecipesAdapter(private val recipe: ArrayList<Recipe>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(var viewBinding: RecipeBinding) : RecyclerView.ViewHolder(viewBinding.root) {
      //  val image_view: ImageView = TODO()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //var image_view: ImageView
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder
        //itemViewHolder.viewBinding.titleTextview.text = recipe[position].title
        //itemViewHolder.viewBinding.servesTextview.text = recipe[position].serves
        //itemViewHolder.viewBinding.ingredientsTextview.text = recipe[position].ingredients.toString()
        //itemViewHolder.viewBinding.employeeAgeTextview.text = recipe[position].cookingTime
        //Picasso.get().load(recipe[position].img).into(holder.image_view)
        //itemViewHolder.viewBinding.imageView.

    }

    override fun getItemCount(): Int {
        return recipe.size
    }
}