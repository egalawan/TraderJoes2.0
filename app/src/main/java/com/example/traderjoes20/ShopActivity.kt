package com.example.traderjoes20

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traderjoes20.databinding.ActivityShopBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

@DelicateCoroutinesApi
class ShopActivity : AppCompatActivity() {

    var itemsArray: ArrayList<Cell> = ArrayList()
    lateinit var adapter: RVAdapter

    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Clean TextViews
        //binding.jsonResultsTextview.text = ""

        setupRecyclerView()
        parseJSON()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.jsonResultsRecyclerview.layoutManager = layoutManager
        binding.jsonResultsRecyclerview.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(
                binding.jsonResultsRecyclerview.context,
                layoutManager.orientation
            )
        ContextCompat.getDrawable(this, R.drawable.line_divider)
            ?.let { drawable -> dividerItemDecoration.setDrawable(drawable) }
        binding.jsonResultsRecyclerview.addItemDecoration(dividerItemDecoration)
    }

    @SuppressLint("LongLogTag")
    private fun parseJSON() {
        GlobalScope.launch(Dispatchers.IO) {
            val url =
                URL("https://raw.githubusercontent.com/egalawan/TraderJoes2.0/main/app/src/main/assets/nested.json")

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
                    //val prettyJson = gson.toJson(JsonParser.parseString(response))
                    //Log.d("Pretty Printed JSON :", prettyJson)
                   // binding.jsonResultsTextview.text = prettyJson

                    val jsonObject = JSONTokener(response).nextValue() as JSONObject

                    val jsonArray = jsonObject.getJSONArray("data")

                    for (i in 0 until jsonArray.length()) {
                        //getJSONObject(for each i).getString or .getJSONObject
                        // ID
                        val id = jsonArray.getJSONObject(i).getString("id")

                        // ingredient
                        val ingredient = jsonArray.getJSONObject(i).getJSONObject("ingredient")

                        // ingredient Name
                        val itemName = ingredient.getString("item")

                        // ingredient Name
                        val itemURL = ingredient.getString("item")

                        // ingredient price
                        val itemPrice = ingredient.getJSONObject("price")

                        // item price in USD
                        val itemUsd = itemPrice.getInt("usd")


                        // ingredient Age
                        val itemWeight = ingredient.getString("weight")

                        val model = Cell(
                            id,
                            itemURL,
                            itemName,
                            "$ $itemUsd",
                            itemWeight
                        )
                        itemsArray.add(model)

                        adapter = RVAdapter(itemsArray)
                        adapter.notifyDataSetChanged()
                    }

                    binding.jsonResultsRecyclerview.adapter = adapter
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }
}