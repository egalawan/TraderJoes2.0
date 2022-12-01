package com.example.traderjoes20

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.traderjoes20.databinding.ActivityPantryBinding

class PantryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantryBinding
    lateinit var ItemView: ListView
    lateinit var addBtn: Button
    lateinit var itemEdt: EditText
    lateinit var ItemList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPantryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // on below line we are initializing our variables.
        ItemView = findViewById(R.id.userlist)
        addBtn = findViewById(R.id.button)
        itemEdt = findViewById(R.id.AddItem)
        ItemList = ArrayList()

        // on below line we are adding items to our list
        ItemList.add("Corn on the Cob")
        ItemList.add("Baby Shanghai Bok Choy")
        //ItemList.add()

        // on below line we are initializing adapter for our list view.
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@PantryActivity,
            android.R.layout.simple_list_item_1,
            ItemList as List<String?>
        )

        // on below line we are setting adapter for our list view.
        ItemView.adapter = adapter

        // on below line we are adding click listener for our button.
        addBtn.setOnClickListener {

            // on below line we are getting text from edit text
            val item = itemEdt.text.toString()

            // on below line we are checking if item is not empty
            if (item.isNotEmpty()) {
                // on below line we are adding item to our list.
                ItemList.add(item)

                // on below line we are notifying adapter
                // that data in list is updated to update our list view.
                adapter.notifyDataSetChanged()
            }
        }

        val arrayAdapter: ArrayAdapter<*>
        val items = arrayOf("Apple","Orange","Organic Banana","Water Melon", "Apricot", "Pineapple", "Mangos", "Campari Tomatoes", "Organic Strawberries 1 Lb ", "Strawberries 2 Lb ", "Strawberries 1 Lb", "Organic Bananas",
            "Herbs of provence", " Shishito Peppers ", "Shiitake Mushrooms ", " Organic Brussels Sprouts ", "Organic Sweet Potatoes", "Sweet Potato ", "Steamed Lentils\n", "Wild Arugula ",
            "Green Onions ", "Organic Carrots of Many Colors", "Organic Mini Sweet Peppers", "Organic Baby Spinach & Spring ", " Baby Cauliflower ", "Organic Petite Potato Medley", "Ready Veggies",
            "Baby Shanghai Bok Choy", " Riced Cauliflower ", " Russet Potatoes ", "Sugar Snap Peas", " Organic Red Bell Peppers ",  " Portabella Mushrooms ", "Organic Shredded Kale",
            "Organic Baby Lettuce Mix ", " Organic Arugula", " Corn on the Cob ",  "Super Sweet Fresh Corn ", "Broccoli & Kale Slaw ", "Organic Persian Cucumbers\n ",
            "Organic Shredded Green & Red ",
            )

        //val mListView = findViewById<ListView>(R.id.userlist)
        //arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        //mListView.adapter = arrayAdapter

    }
}
