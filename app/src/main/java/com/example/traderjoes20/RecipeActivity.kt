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
        val directions = bundle!!.getString("directions")
        val ingredients = bundle!!.getString("ingredients")
        val img = bundle!!.getString("img")
        //Log.i("HELLOOOOO",img.toString())
        val serves = bundle!!.getString("serves")
        val title = bundle!!.getString("title")
        val cookingTime = bundle!!.getString("cookingTime")
        val prepTime = bundle!!.getString("prepTime")

        recipeTitle.text = title
        recipeServes.text = serves
        recipeCookingTime.text = cookingTime
        recipePrepTime.text = prepTime
        recipeDirections.text = directions
        recipeIngredients.text = ingredients
        //Log.i("img", img.toString())
        //recipeImage.setImageResource(img.toInt())
        Picasso.get().load(img)
            .placeholder(R.drawable.first_food)
            .into(recipeImageView)

    }
}