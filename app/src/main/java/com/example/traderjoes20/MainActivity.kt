package com.example.traderjoes20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.Adapters.RandomRecipeAdapter
import com.example.traderjoes20.Listeners.RandomRecipeResponseListener
import com.example.traderjoes20.Models.RandomRecipeApiResponse
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    private var manager: RequestManager? = null
    var randomRecipeAdapter: RandomRecipeAdapter? = null
    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = RequestManager(this)
        manager!!.getRandomRecipes(randomRecipeResponseListener)

        //each button functionality
        val buttonToPantry = findViewById<Button>(R.id.btnMyPantry)
        buttonToPantry.setOnClickListener{
            val intent = Intent(this, PantryActivity::class.java)
            startActivity(intent)
        }

        val buttonToRecipeHub = findViewById<Button>(R.id.btnRecipeHub)
        buttonToRecipeHub.setOnClickListener{
            val intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }

        val buttonToShop = findViewById<Button>(R.id.btnShop)
        buttonToShop.setOnClickListener{
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
    }

    private val randomRecipeResponseListener: RandomRecipeResponseListener =
        object : RandomRecipeResponseListener {
            override fun didFetch(response: RandomRecipeApiResponse?, message: String?) {
                recyclerView = findViewById(R.id.recycler_random)
                recyclerView!!.setHasFixedSize(true)
                recyclerView!!.layoutManager = GridLayoutManager(this@MainActivity, 1)
                randomRecipeAdapter =
                    response!!.recipes?.let { RandomRecipeAdapter(this@MainActivity, it) }
                recyclerView!!.adapter = randomRecipeAdapter
            }

            override fun didError(message: String?) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
}