package com.example.traderjoes20

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyPantryActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    private lateinit var pantryList:ListView
    private lateinit var addItem:EditText
    private lateinit var addButton:Button
    private lateinit var backHome:Button
    var itemList: ArrayList<Cell> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pantry)
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference

        //each "item" in the MyPantry View
        pantryList = findViewById(R.id.pantryList)
        addItem = findViewById(R.id.AddItem)
        addButton = findViewById(R.id.button)
        backHome = findViewById(R.id.backToHomeFromPantry)

        val items = mutableListOf<String>()
        val adapter = PantryListAdapter(this, items)
        pantryList.adapter = adapter

        //sends to firebase console
        database = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("pantry")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items.clear()
                for (ds in dataSnapshot.children) {
                    val item = ds.getValue(String::class.java)
                    items.add(item!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })

        //adding whatever is in the text box
        addButton.setOnClickListener {
            val item = addItem.text.toString()
            database.push().setValue(item)
            addItem.text.clear()
            Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show()
        }

        //go back home button
        backHome.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        //go to the grocery list
        val btnGroceryList = findViewById<Button>(R.id.btnGroceryList)
        btnGroceryList.setOnClickListener {
            startActivity(Intent(this, GroceryListActivity::class.java))
        }
    }
}