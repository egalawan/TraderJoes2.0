package com.example.traderjoes20

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.traderjoes20.databinding.ActivityListgroceryBinding

class GroceryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListgroceryBinding
    lateinit var ItemView: ListView
    lateinit var addBtn: Button
    lateinit var itemEdt: EditText
    lateinit var ItemList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListgroceryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // on below line we are initializing our variables.
        ItemView = findViewById(R.id.userlist)
        addBtn = findViewById(R.id.button)
        itemEdt = findViewById(R.id.AddItem)
        ItemList = ArrayList()

        // on below line we are adding items to our list
        ItemList.add("Corn on the Cob")
        ItemList.add("Baby Shanghai Bok Choy")
        ItemList.add("Sweet Potato ")
        ItemList.add("Shiitake Mushrooms ")
        ItemList.add("Green Onions ")
        ItemList.add("Super Sweet Fresh Corn ")

        // on below line we are initializing adapter for our list view.
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@GroceryListActivity,
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

    }
}
