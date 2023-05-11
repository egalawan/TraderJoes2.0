package com.example.traderjoes20

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi

//page for single recipe
class RecipeActivity : AppCompatActivity(){
    private lateinit var recipeImageView: ImageView
    private lateinit var recipeTitle: TextView
    private lateinit var recipeServes: TextView
    private lateinit var recipeCookingTime: TextView
    private lateinit var recipePrepTime: TextView
    private lateinit var recipeDirections: TextView
    private lateinit var recipeIngredients: TextView
    private lateinit var backButton: Button
    private lateinit var sendToGrocery:Button
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    @OptIn(DelicateCoroutinesApi::class)
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
        backButton = findViewById(R.id.backToRecipes)
        sendToGrocery = findViewById(R.id.btnGroceryList)

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference


        val bundle : Bundle? = intent.extras
        val title = bundle?.getString("title")

        val img = bundle?.getString("img")
        val serves = bundle?.getString("serves")

        val cookingTime = bundle?.getString("cookingTime")
        val prepTime = bundle?.getString("prepTime")

        recipeTitle.text = title
        recipeServes.text = if (serves?.trim()?.isEmpty() == true) "n/a" else serves
        recipeCookingTime.text = if (cookingTime?.trim()?.isEmpty() == true) "n/a" else cookingTime
        recipePrepTime.text = if (prepTime?.trim()?.isEmpty() == true) "n/a" else prepTime


        val ingredients = bundle?.getString("ingredients")
        val ingredientsList = ingredients?.split("\n")
        val formattedIngredients = StringBuilder()
        if (ingredientsList != null) {
            for (ingredient in ingredientsList) {
                formattedIngredients.append("$ingredient\n")
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
            .into(recipeImageView)

        //need to add function for back Home Everytime
        backButton.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        sendToGrocery.setOnClickListener {
            val userRef = database.child("users").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val databaseReference: DatabaseReference = if (dataSnapshot.child("groupCode").exists()) {
                        // User is in a group, retrieve the group code
                        val groupCode = dataSnapshot.child("groupCode").value.toString()
                        // Update the database reference to the group's grocery list
                        database.child("groups").child(groupCode).child("grocery-list")
                    } else {
                        // User is not in a group, use the user's own grocery list
                        database.child("users").child(userId).child("grocery-list")
                    }

                    for (ingredient in ingredientsList!!) {
                        databaseReference.push().setValue(ingredient)
                    }
                    Toast.makeText(this@RecipeActivity, "Item added!", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Send to Grocery", "Failed to add item", databaseError.toException())
                }
            })
        }

    }
}