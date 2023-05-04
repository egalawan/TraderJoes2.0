package com.example.traderjoes20

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traderjoes20.databinding.ActivityRecipesBinding
import com.example.traderjoes20.RecipeAdapter.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/*
    main hub page
     */
@DelicateCoroutinesApi
class RecipesActivity : AppCompatActivity(), RecipeAdapter.OnItemClickListener {
    private var recipeItems: ArrayList<Recipes> = ArrayList()
    private lateinit var adapter: RecipeAdapter
    private lateinit var binding: ActivityRecipesBinding
    private lateinit var back:ImageButton
    private lateinit var searchView: SearchView
    private var filteredItems: List<Recipes> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setupRecyclerView()
        //the json parse
        parseJSON(this)

//      recipeSearch = findViewById(R.id.Recipe_SearchView)
        back = findViewById(R.id.backToMain)
        //recipeSearch = binding.RecipeSearchView
        searchView = findViewById(R.id.searchView)

        searchView.queryHint = "Search recipes..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.search(newText)
                return true
            }
        })

        //go back home button
        back.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
    //END OF ON-CREATE

    //FUNCTIONS:BELOW
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(
                binding.recyclerView.context,
                layoutManager.orientation
            )
        ContextCompat.getDrawable(this, R.drawable.line_divider)
            ?.let { drawable -> dividerItemDecoration.setDrawable(drawable) }
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
    }

    @SuppressLint("LongLogTag")
    private fun parseJSON(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val jsonString = try {
                val inputStream = context.assets.open("recipes.json")
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
            for (i in 0 until jsonArray.length()) {
                val img = jsonArray.getJSONObject(i).getString("img")
                val ingredientsArray = jsonArray.getJSONObject(i).getJSONArray("ingredients")
                val ingredients = mutableListOf<String>()
                for (j in 0 until ingredientsArray.length()) {
                    ingredients.add(ingredientsArray.getString(j))
                }
                val serves = jsonArray.getJSONObject(i).getString("serves")
                val tagIds = jsonArray.getJSONObject(i).getJSONArray("tagIds")
                val title = jsonArray.getJSONObject(i).getString("title")
                val directions = jsonArray.getJSONObject(i).getString("directions")
                val cookingTime = jsonArray.getJSONObject(i).getString("cookingTime")
                val prepTime = jsonArray.getJSONObject(i).getString("prepTime")
                val id = jsonArray.getJSONObject(i).getString("id")

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
                recipeItems.add(model)
            }
            adapter = RecipeAdapter(filteredItems, recipeItems, this@RecipesActivity)

            binding.recyclerView.post {
                adapter.notifyDataSetChanged()
            }
            withContext(Dispatchers.Main) {
                binding.recyclerView.adapter = adapter
            }
        }
    }
    private fun filterRecipes(query: String?) {
        filteredItems = if (query.isNullOrEmpty()) {
            recipeItems
        } else {
            recipeItems.filter {
                it.title!!.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        }
        adapter.setItems(filteredItems)
    }

    /*
            This is the code for when one of the recipes is clicked
             */
    override fun onItemClick(position: Int) {

        //adapter instead of RecipeAdapter because we defined early
        val filteredList = adapter.getFilteredRecipeList()

        val intent = Intent(this@RecipesActivity, RecipeActivity::class.java)
        intent.putExtra("directions", filteredList[position].directions)
        val ingredientsString = filteredList[position].ingredients.joinToString("\n")
        intent.putExtra("ingredients", ingredientsString)
        //intent.putExtra("ingredients", recipeItems[position].ingredients as ArrayList<String>)
        intent.putExtra("img", filteredList[position].img)
        intent.putExtra("serves", filteredList[position].serves)
        intent.putExtra("title", filteredList[position].title)
        intent.putExtra("cookingTime", filteredList[position].cookingTime)
        intent.putExtra("prepTime", filteredList[position].prepTime)

        startActivity(intent)

        adapter.notifyItemChanged(position)
    }

}



