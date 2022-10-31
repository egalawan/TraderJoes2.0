package com.example.traderjoes20.Adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.Adapters.RandomRecipeViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.traderjoes20.R
import com.squareup.picasso.Picasso
import androidx.cardview.widget.CardView
import android.widget.TextView
import com.example.traderjoes20.Models.Recipe

public class RandomRecipeAdapter(var context: Context, var list: List<Recipe>) :
    RecyclerView.Adapter<RandomRecipeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomRecipeViewHolder {
        return RandomRecipeViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RandomRecipeViewHolder, position: Int) {
        holder.textView_title.text = list[position].title
        holder.textView_title.isSelected = true
        holder.textView_likes.text = list[position].aggregateLikes.toString() + " Likes"
        holder.textView_servings.text = list[position].servings.toString() + " Servings"
        holder.textView_time.text = list[position].readyInMinutes.toString() + " Minutes"
        Picasso.get().load(list[position].image).into(holder.imageView_food)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class RandomRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var random_list_container: CardView
    var textView_title: TextView
    var textView_servings: TextView
    var textView_likes: TextView
    var textView_time: TextView
    var imageView_food: ImageView

    //constructors
    init {
        random_list_container = itemView.findViewById(R.id.random_list_container)
        textView_title = itemView.findViewById(R.id.textView_title)
        textView_servings = itemView.findViewById(R.id.textView_servings)
        textView_likes = itemView.findViewById(R.id.textView_likes)
        textView_time = itemView.findViewById(R.id.textView_time)
        imageView_food = itemView.findViewById(R.id.imageView_food)
    }
}