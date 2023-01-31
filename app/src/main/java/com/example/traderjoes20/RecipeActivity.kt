package com.example.traderjoes20

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class RecipeActivity : AppCompatActivity(){
    lateinit var recipeImageView: ImageView
    lateinit var recipeTitle: TextView
    lateinit var recipeServes: TextView
    lateinit var recipeCookingTime: TextView
    lateinit var recipePrepTime: TextView
    lateinit var recipeDirections: TextView
    lateinit var recipeIngredients: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        recipeTitle  = findViewById(R.id.Recipe_title_Textview)
        recipeServes= findViewById(R.id.Recipe_serves_Textview)
        recipeCookingTime = findViewById(R.id.Recipe_cookingTime_Textview)
        recipePrepTime = findViewById(R.id.Recipe_prepTime_Textview)
        recipeDirections = findViewById(R.id.Recipe_directions_Textview)
        recipeIngredients = findViewById(R.id.Recipe_ingredients_Textview)
        recipeImageView = findViewById(R.id.Recipe_ImageView)

        val bundle : Bundle? = intent.extras
        val title = bundle?.getString("title")

        val img = bundle?.getString("img")
        val serves = bundle?.getString("serves")

        val cookingTime = bundle?.getString("cookingTime")
        val prepTime = bundle?.getString("prepTime")

        recipeTitle.text = title
        recipeServes.text = if (serves?.trim()?.isEmpty() == true) "-" else serves
        recipeCookingTime.text = if (cookingTime?.trim()?.isEmpty() == true) "-" else cookingTime
        recipePrepTime.text = if (prepTime?.trim()?.isEmpty() == true) "-" else prepTime


        val ingredients = bundle?.getString("ingredients")
        val ingredientsList = ingredients?.split("\n")
        val formattedIngredients = StringBuilder()
        if (ingredientsList != null) {
            for (ingredient in ingredientsList) {
                formattedIngredients.append("- $ingredient\n")
            }
            recipeIngredients.text = formattedIngredients.toString()
        }

        //for directions
        val directions = bundle?.getString("directions")
        val directionsList = directions?.split("\n")
        val formattedDirections = StringBuilder()
        for ((index, direction) in directionsList!!.withIndex()) {
            if (direction.isNotEmpty() && direction != ".") {
                formattedDirections.append("${index + 1}. $direction\n")
            }
        }
        recipeDirections.text = formattedDirections.toString()

        //

        Picasso.get().load(img)
            .placeholder(R.drawable.first_food)
            .into(recipeImageView)
    }
}