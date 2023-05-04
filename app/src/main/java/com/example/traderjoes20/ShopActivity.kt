package com.example.traderjoes20

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traderjoes20.databinding.ActivityShopBinding
import com.example.traderjoes20.databinding.ActivityMyPantryBinding.*
import com.example.traderjoes20.databinding.CellBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@DelicateCoroutinesApi
class ShopActivity : AppCompatActivity(){

    var itemsArray: ArrayList<Cell> = ArrayList()
    private lateinit var adapter: RVAdapter
    private lateinit var binding: ActivityShopBinding
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var backHome:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference
        setupRecyclerView()
        parseJSON(this)

        //TOP is supposed to be the button for send to pantry list
        backHome = findViewById(R.id.backToHomeFromShop)
        //go back home button
        backHome.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

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

    @SuppressLint("LongLogTag", "NotifyDataSetChanged")
    private fun parseJSON(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val jsonString = try {
                val inputStream = context.assets.open("nested.json")
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val jsonString = bufferedReader.use { it.readText() }
                inputStream.close()
                jsonString
            } catch (ioException: IOException) {
                Log.e("JSON FILE", "Error opening JSON file", ioException)
                return@launch
            }

            val jsonObject = JSONTokener(jsonString).nextValue() as JSONObject

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
                val itemURL = jsonArray.getJSONObject(i).getString("url")

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
            withContext(Dispatchers.Main) {
                binding.jsonResultsRecyclerview.adapter = adapter
            }
        }
    }
    //end of parseJSON
}
