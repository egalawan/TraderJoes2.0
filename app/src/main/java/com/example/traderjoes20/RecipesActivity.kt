package com.example.traderjoes20

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traderjoes20.databinding.ActivityRecipesBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


@DelicateCoroutinesApi
class RecipesActivity : AppCompatActivity() {

    var recipeItems: ArrayList<Recipes> = ArrayList()
    lateinit var adapter: RecipeAdapter

    private lateinit var binding: ActivityRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //val linearLayout = findViewById<LinearLayout>(R.id.this_linearLayout)
        //val imageView = ImageView(this)

       // Glide.with(this).load("https://s3.amazonaws.com/appsdeveloperblog/Micky.jpg").into(imageView)\
//        Picasso.with(this).load().into(imageView)
//        linearLayout.addView(imageView)
        setupRecyclerView()
        parseJSON()

    }

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
    private fun parseJSON() {
        GlobalScope.launch(Dispatchers.IO) {
            val url =
                URL("https://raw.githubusercontent.com/egalawan/TraderJoes2.0/main/app/src/main/assets/recipes.json")

            val httpsURLConnection = url.openConnection() as HttpsURLConnection
            httpsURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpsURLConnection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    // Convert raw JSON to pretty JSON using GSON library
                    //val gson = GsonBuilder().setPrettyPrinting().create()
                    //val prettyJson = gson.toJson(JsonParser.parseString(response))
                    //Log.d("Pretty Printed JSON :", prettyJson)
                    // binding.jsonResultsTextview.text = prettyJson

                    val jsonObject = JSONTokener(response).nextValue() as JSONObject

                    val jsonArray = jsonObject.getJSONArray("recipes")


                    //jsonArray.length()
                    for (i in 0 until jsonArray.length()) {
                        //getJSONObject(for each i).getString or .getJSONObject
                        //img
                        val img = jsonArray.getJSONObject(i).getString("img")

                        //tags
                        //val tags = jsonArray.getJSONObject(i).getJSONArray("tags")

                        //tagId
                        //val tagId = tags.getInt("tagId".toInt())
                        //name
                        //val name = tags.getString("name".toInt())
                        //val ingredients: ArrayList<String> = ArrayList()

                        //OG
                        //ingredients

                        val ingredients = jsonArray.getJSONObject(i).getString("ingredients")

                        //val ingredients= jsonArray.getJSONObject(i).getJSONArray("ingredients")
                        //ingredients.

                        val bracket = "["
                        val bracket2 = "]"
                        val comma = ","
                        val space = ""
                        val indent = "\n"

                        val newIngredients = ingredients.replace(comma,indent)
                        val newIng = newIngredients.replace(bracket,space)
                        val finalIngredients = newIng.replace(bracket2,space)

                        // serves
                        val serves = jsonArray.getJSONObject(i).getString("serves")

                        // tagIds
                        val tagIds = jsonArray.getJSONObject(i).getJSONArray("tagIds")

                        //title
                        val title = jsonArray.getJSONObject(i).getString("title")
                        //Log.i("title: ", title)
                        //directions
                        val directions = jsonArray.getJSONObject(i).getString("directions")

                        //cookingTime
                        val cookingTime = jsonArray.getJSONObject(i).getString("cookingTime")

                        //prepTime
                        val prepTime = jsonArray.getJSONObject(i).getString("prepTime")

                        //id
                        val id = jsonArray.getJSONObject(i).getString("id")
                        //Log.i("id: ", id)

                        val model = Recipes(
                            img,
                            //tagId,
                            //name,
                            finalIngredients,
                            serves,
                            tagIds,
                            title,
                            directions,
                            cookingTime,
                            prepTime,
                            id,
                        )
                        recipeItems.add(model)

                        adapter = RecipeAdapter(recipeItems)
                        adapter.notifyDataSetChanged()
                    }

                    binding.recyclerView.adapter = adapter
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }

}

