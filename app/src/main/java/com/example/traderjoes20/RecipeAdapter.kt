package com.example.traderjoes20

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.databinding.AllRecipesBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList

/*
    On Click next Page after Recipe Hub
     */
class RecipeAdapter @OptIn(DelicateCoroutinesApi::class) constructor(
    private var items: List<Recipes>,
    private val recipe: ArrayList<Recipes>,
    private val listener: RecipesActivity,
    //can set to RecipeActivity but if want to reuse use the interface we created because you can pass any object that implements this
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredRecipeList: ArrayList<Recipes> = recipe
    fun search(query: String?) {
        if (query != null) {
            Log.d("search: ",query )
        }
        filteredRecipeList = if (query.isNullOrEmpty()) {
            recipe
        } else {
            val filteredList = ArrayList<Recipes>()
            for (recipe in recipe) {
                if (recipe.title?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true)
                {
                    filteredList.add(recipe)
                }
            }
            filteredList
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = AllRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*
        Shows what is on the Recipe Hub page
         */
        val recipeViewHolder = holder as RecipeAdapter.RecipeViewHolder
        val title = filteredRecipeList[position].title
        //val url = recipe[position].img

        Log.d("RecipeAdapter", "Recipe title at position $position: $title")

        val ingredientsString = StringBuilder()
        for (ingredient in recipe[position].ingredients) {
            ingredientsString.append("- $ingredient\n")
        }

        recipeViewHolder.recipeIngredientsTextview.text = ingredientsString.toString()
        recipeViewHolder.recipeTitleTextview.text = filteredRecipeList[position].title
        Picasso.get().load(filteredRecipeList[position].img).into(recipeViewHolder.viewBinding.imageView)

        //Picasso.get().load(url).into(recipeViewHolder.viewBinding.imageView)
        //recipeViewHolder.recipeDirectionsTextview.text = recipe[position].directions
        //recipeViewHolder.viewBinding.RecipeServesTextview.text = recipe[position].serves
        //recipeViewHolder.viewBinding.RecipeCookingTimeTextview.text = recipe[position].cookingTime
        //recipeViewHolder.viewBinding.RecipePrepTimeTextview.text = recipe[position].prepTime
    }

    override fun getItemCount(): Int {
        return filteredRecipeList.size
    }

    fun getItem(position: Int): Recipes {
        return filteredRecipeList[position]
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

    fun setItems(newItems: List<Recipes>) {
        items = newItems
        notifyDataSetChanged()
    }
    //
}


