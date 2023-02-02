package com.example.traderjoes20

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyPantryActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private var adapter: ArrayAdapter<String>? = null
    private var items = ArrayList<String>()
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pantry)
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference

        val pantryList = findViewById<ListView>(R.id.pantryList)
        val addItem = findViewById<EditText>(R.id.AddItem)
        val addButton = findViewById<Button>(R.id.button)

        val items = mutableListOf<String>()
        val adapter = PantryListAdapter(this, items)
        pantryList.adapter = adapter


        database.child("pantry").addValueEventListener(object : ValueEventListener {
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

        addButton.setOnClickListener {
            val item = addItem.text.toString()
            database.child("pantry").push().setValue(item)
            addItem.text.clear()
            Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show()
        }

        val btnGroceryList = findViewById<Button>(R.id.btnGroceryList)
        btnGroceryList.setOnClickListener {
            startActivity(Intent(this, GroceryListActivity::class.java))
        }
    }
}