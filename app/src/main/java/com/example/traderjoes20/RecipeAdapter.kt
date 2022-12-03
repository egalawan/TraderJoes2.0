package com.example.traderjoes20

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.databinding.AllRecipesBinding
import com.squareup.picasso.Picasso

class RecipeAdapter(
    private val recipe: ArrayList<Recipes>,
    private val listener: OnItemClickListener //can set to RecipeActivity but if want to reuse use the interface we created because you can pass any object that implements this
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = AllRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipeViewHolder = holder as RecipeAdapter.RecipeViewHolder
        val url = recipe[position].img
        //Log.i("Url: ", url)

        //Picasso.get().load(url).into(recipeViewHolder.recipeImageView)
        Picasso.get().load(url).into(recipeViewHolder.viewBinding.imageView)

        recipeViewHolder.recipeIngredientsTextview.text = recipe[position].ingredients
        recipeViewHolder.recipeTitleTextview.text = recipe[position].title
        //recipeViewHolder.recipeDirectionsTextview.text = recipe[position].directions
        //recipeViewHolder.viewBinding.RecipeServesTextview.text = recipe[position].serves
        //recipeViewHolder.viewBinding.RecipeCookingTimeTextview.text = recipe[position].cookingTime
        //recipeViewHolder.viewBinding.RecipePrepTimeTextview.text = recipe[position].prepTime
    }

    override fun getItemCount(): Int {
        return recipe.size
    }

    //when OnClickListener is implemented need to add the override fun onClick
    inner class RecipeViewHolder(var viewBinding: AllRecipesBinding) : RecyclerView.ViewHolder(viewBinding.root),
    OnClickListener{
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

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    //names for interface and fun dont matter, function*
    //when you have interface OnItemClickListener you need to the fun onItemClick
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}

