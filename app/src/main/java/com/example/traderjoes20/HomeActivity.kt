package com.example.traderjoes20

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.databinding.ActivityHomeBinding
import com.example.traderjoes20.databinding.ActivityRecipesBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

@DelicateCoroutinesApi
class HomeActivity : AppCompatActivity(), RecipeAdapter.OnItemClickListener {
    private var recipeItems: ArrayList<Recipes> = ArrayList()
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //the json parse
        parseJSON(this)
        //NEED TO SET UP THE RECYCLER VIEW
        setupRecyclerView()

        //each button functionality
        val buttonToPantry = findViewById<Button>(R.id.btnMyPantry)
        buttonToPantry.setOnClickListener{
            val intent = Intent(this, MyPantryActivity::class.java)
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

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerRandom.layoutManager = layoutManager
        binding.recyclerRandom.setHasFixedSize(true)
        // remove divider decoration since there's only one item being shown
    }

    //parse the json file to get one random recipe
    private fun parseJSON(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val jsonString = try {
                val inputStream = context.assets.open("Test.json")
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val jsonString = bufferedReader.use { it.readText() }
                inputStream.close()
                jsonString
            } catch (ioException: IOException) {
                Log.e("JSON FILE", "Error opening JSON file", ioException)
                return@launch
            }

            val jsonObject = JSONTokener(jsonString).nextValue() as JSONObject
            val jsonArray = jsonObject.getJSONArray("recipes")

            // randomly select one recipe
            val randomIndex = (0 until jsonArray.length()).random()
            val selectedRecipe = jsonArray.getJSONObject(randomIndex)

            val img = selectedRecipe.getString("img")
            val ingredientsArray = selectedRecipe.getJSONArray("ingredients")
            val ingredients = mutableListOf<String>()
            for (j in 0 until ingredientsArray.length()) {
                ingredients.add(ingredientsArray.getString(j))
            }
            val serves = selectedRecipe.getString("serves")
            val tagIds = selectedRecipe.getJSONArray("tagIds")
            val title = selectedRecipe.getString("title")
            val directions = selectedRecipe.getString("directions")
            val cookingTime = selectedRecipe.getString("cookingTime")
            val prepTime = selectedRecipe.getString("prepTime")
            val id = selectedRecipe.getString("id")

            val model = Recipes(
                img,
                ingredients,
                serves,
                tagIds,
                title,
                directions,
                cookingTime,
                prepTime,
                id,
            )

            // clear the recipeItems list and add the selected recipe
            recipeItems.clear()
            recipeItems.add(model)

            adapter = HomeAdapter(recipeItems,this@HomeActivity)

            binding.recyclerRandom.post {
                adapter.notifyDataSetChanged()
            }
            withContext(Dispatchers.Main) {
                binding.recyclerRandom.adapter = adapter
            }
        }
    }

    //when the recipes are clicked, need to send the information to another page
    override fun onItemClick(position: Int) {
        /*
        This is the code for when one of the recipes is clicked

        Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickItem = recipeItems[position]
        clickItem.ingredients = "Clicked"
         */
        val intent = Intent(this@HomeActivity, RecipeActivity::class.java)
        intent.putExtra("directions", recipeItems[position].directions)
        val ingredientsString = recipeItems[position].ingredients.joinToString("\n")
        intent.putExtra("ingredients", ingredientsString)
        //intent.putExtra("ingredients", recipeItems[position].ingredients as ArrayList<String>)
        intent.putExtra("img", recipeItems[position].img)
        intent.putExtra("serves", recipeItems[position].serves)
        intent.putExtra("title", recipeItems[position].title)
        intent.putExtra("cookingTime", recipeItems[position].cookingTime)
        intent.putExtra("prepTime", recipeItems[position].prepTime)

        startActivity(intent)

        adapter.notifyItemChanged(position)
    }
}