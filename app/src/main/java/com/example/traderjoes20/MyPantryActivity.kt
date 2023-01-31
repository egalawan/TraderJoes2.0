package com.example.traderjoes20

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MyPantryActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pantry)

        val arrayAdapter: ArrayAdapter<*>

        val items = arrayOf("Apple","Orange","Organic Banana","Water Melon", "Apricot", "Pineapple", "Mangos", "Campari Tomatoes", "Organic Strawberries 1 Lb ", "Strawberries 2 Lb ", "Strawberries 1 Lb", "Organic Bananas",
            "Herbs of provence", " Shishito Peppers ", "Shiitake Mushrooms ", " Organic Brussels Sprouts ", "Organic Sweet Potatoes", "Sweet Potato ", "Steamed Lentils\n", "Wild Arugula ",
            "Green Onions ", "Organic Carrots of Many Colors", "Organic Mini Sweet Peppers", "Organic Baby Spinach & Spring ", " Baby Cauliflower ", "Organic Petite Potato Medley", "Ready Veggies",
            "Baby Shanghai Bok Choy", " Riced Cauliflower ", " Russet Potatoes ", "Sugar Snap Peas", " Organic Red Bell Peppers ",  " Portabella Mushrooms ", "Organic Shredded Kale",
            "Organic Baby Lettuce Mix ", "Organic Arugula", " Corn on the Cob ",  "Super Sweet Fresh Corn ", "Broccoli & Kale Slaw ", "Organic Persian Cucumbers\n ",
            "Organic Shredded Green & Red ",
        )

        val mListView = findViewById<ListView>(R.id.pantryList)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        mListView.adapter = arrayAdapter
        //From My Pantry to Grocery List
        val buttonToGroceryList = findViewById<Button>(R.id.btnGroceryList)
        buttonToGroceryList.setOnClickListener{
            val intent = Intent(this, GroceryListActivity::class.java)
            startActivity(intent)
        }
    }
}