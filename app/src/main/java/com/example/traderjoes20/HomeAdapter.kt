package com.example.traderjoes20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.databinding.AllRecipesBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi

class HomeAdapter @OptIn(DelicateCoroutinesApi::class) constructor(

    private val recipe: ArrayList<Recipes>,
    private val listener: HomeActivity,
):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = AllRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*
        Shows what is on the Recipe Hub page
         */
        val recipeViewHolder = holder as HomeAdapter.RecipeViewHolder
        //val url = recipe[position].img

        val ingredientsString = StringBuilder()
        for (ingredient in recipe[position].ingredients) {
            ingredientsString.append("- $ingredient\n")
        }
        recipeViewHolder.recipeIngredientsTextview.text = ingredientsString.toString()
        recipeViewHolder.recipeTitleTextview.text = recipe[position].title
        Picasso.get().load(recipe[position].img).into(recipeViewHolder.viewBinding.imageView)
    }

    override fun getItemCount(): Int {
        return recipe.size
    }

    //when OnClickListener is implemented need to add the override fun onClick
    inner class RecipeViewHolder(var viewBinding: AllRecipesBinding) : RecyclerView.ViewHolder(viewBinding.root),
        View.OnClickListener {
        val recipeIngredientsTextview: TextView = viewBinding.RecipeIngredientsTextview
        val recipeTitleTextview: TextView = viewBinding.RecipeTitleTextview
        //val recipeDirectionsTextview: TextView = viewBinding.RecipeDirectionsTextview
        //var recipeImageView: ImageView

        init {
            //recipeImageView = viewBinding.imageView
            viewBinding.RecipeTitleTextview.setOnClickListener(this)
            viewBinding.RecipeIngredientsTextview.setOnClickListener(this)
            //viewBinding.RecipeDirectionsTextview.setOnClickListener(this)
        }

        @OptIn(DelicateCoroutinesApi::class)
        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    //when you have interface OnItemClickListener you need to the fun onItemClick
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun updateData(newData: List<Recipes>) {
        var recipe = newData
        notifyDataSetChanged()
    }

}
