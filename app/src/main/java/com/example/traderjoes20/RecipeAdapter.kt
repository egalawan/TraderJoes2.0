package com.example.traderjoes20

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.databinding.AllRecipesBinding

class RecipeAdapter(private val recipe: ArrayList<Recipes>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ItemViewHolder(var viewBinding: AllRecipesBinding) : RecyclerView.ViewHolder(viewBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = AllRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ItemViewHolder

        itemViewHolder.viewBinding.RecipeIngredientsTextview.text = recipe[position].ingredients
        //itemViewHolder.viewBinding.RecipeServesTextview.text = recipe[position].serves
        itemViewHolder.viewBinding.RecipeTitleTextview.text = recipe[position].title
        itemViewHolder.viewBinding.RecipeDirectionsTextview.text = recipe[position].directions
        //itemViewHolder.viewBinding.RecipeCookingTimeTextview.text = recipe[position].cookingTime
        //itemViewHolder.viewBinding.RecipePrepTimeTextview.text = recipe[position].prepTime

        //Picasso.get().load(recipe[position].img).into(itemViewHolder.viewBinding.RecipeImageView)

    }

    override fun getItemCount(): Int {
        return recipe.size
    }
}
