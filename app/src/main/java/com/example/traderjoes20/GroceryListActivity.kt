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
    private lateinit var itemView: ListView
    private lateinit var addBtn: Button
    private lateinit var itemEdt: EditText
    private lateinit var itemList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListgroceryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // on below line we are initializing our variables.
        itemView = findViewById(R.id.userlist)
        addBtn = findViewById(R.id.button)
        itemEdt = findViewById(R.id.AddItem)
        itemList = ArrayList()

        // on below line we are adding items to our list
        itemList.add("Corn on the Cob")
        itemList.add("Baby Shanghai Bok Choy")
        itemList.add("Sweet Potato ")
        itemList.add("Shiitake Mushrooms ")
        itemList.add("Green Onions ")
        itemList.add("Super Sweet Fresh Corn ")

        // on below line we are initializing adapter for our list view.
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this@GroceryListActivity,
            android.R.layout.simple_list_item_1,
            itemList as List<String?>
        )

        // on below line we are setting adapter for our list view.
        itemView.adapter = adapter

        // on below line we are adding click listener for our button.
        addBtn.setOnClickListener {

            // on below line we are getting text from edit text
            val item = itemEdt.text.toString()

            // on below line we are checking if item is not empty
            if (item.isNotEmpty()) {
                // on below line we are adding item to our list.
                itemList.add(item)

                // on below line we are notifying adapter
                // that data in list is updated to update our list view.
                adapter.notifyDataSetChanged()
            }
        }

    }
}
