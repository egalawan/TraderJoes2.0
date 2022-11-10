package com.example.traderjoes20

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traderjoes20.databinding.ActivityRecipesBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

@DelicateCoroutinesApi
class RecipesActivity : AppCompatActivity() {

    var itemsArray: ArrayList<Cell> = ArrayList()
    lateinit var adapter: RVAdapter

    private lateinit var binding: ActivityRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Clean TextViews
        binding.RecipeTextview.text = ""

        setupRecyclerView()
        parseJSON()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.RecipesRecyclerview.layoutManager = layoutManager
        binding.RecipesRecyclerview.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(
                binding.RecipesRecyclerview.context,
                layoutManager.orientation
            )
        ContextCompat.getDrawable(this, R.drawable.line_divider)
            ?.let { drawable -> dividerItemDecoration.setDrawable(drawable) }
        binding.RecipesRecyclerview.addItemDecoration(dividerItemDecoration)
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
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(JsonParser.parseString(response))
                    Log.d("Pretty Printed JSON :", prettyJson)
                    binding.RecipeTextview.text = prettyJson

                    val jsonObject = JSONTokener(response).nextValue() as JSONObject

                    val jsonArray = jsonObject.getJSONArray("recipes")

                    for (i in 0 until jsonArray.length()) {
                        //getJSONObject(for each i).getString or .getJSONObject
                        // ID
                        val img = jsonArray.getJSONObject(i).getString("img")
                        Log.i("img: ", img)

                        // tags
                        val tags = jsonArray.getJSONObject(i).getJSONObject("tags")

                        // Recipe title
                        val title = tags.getString("title")
                        Log.i("Recipe Name: ", title)

                        //another object

                        val tagIds = tags.getJSONObject("salary")

                        // Employee Salary in USD
                        val employeeSalaryUSD = employeeSalary.getInt("usd")
                        Log.i("Employee Salary in USD: ", employeeSalaryUSD.toString())

                        // Employee Salary in EUR
                        val employeeSalaryEUR = employeeSalary.getInt("eur")
                        Log.i("Employee Salary: ", employeeSalaryEUR.toString())

                        //
                        // Employee Age
                        val employeeAge = tags.getString("age")
                        Log.i("Employee Age: ", employeeAge)

                        //above is all information from each data block from json file, change to our json file
                        val cookingTime,
                        val directions,
                        val ingredients,
                        val prepTime,
                        val serves,
                        val tagIds,
                        val id

                        val model = Recipe(
                            cookingTime,
                            directions,
                            img,
                            id,
                            ingredients,
                            prepTime,
                            serves,
                            tagIds,
                            tags,
                            title
                        )
                        itemsArray.add(model)

                        adapter = RecipesAdapter(itemsArray)
                        adapter.notifyDataSetChanged()
                    }

                    binding.RecipesRecyclerview.adapter = adapter
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }
}