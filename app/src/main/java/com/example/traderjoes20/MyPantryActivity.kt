package com.example.traderjoes20

import android.app.AlertDialog
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
    private lateinit var groupCode: String

    private lateinit var pantryList: ListView
    private lateinit var addItem: EditText
    private lateinit var addButton: Button
    private lateinit var backHome: Button
    private var itemList: ArrayList<Cell> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pantry)
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference

        // Each "item" in the MyPantry View
        pantryList = findViewById(R.id.pantryList)
        addItem = findViewById(R.id.AddItem)
        addButton = findViewById(R.id.button)
        backHome = findViewById(R.id.backToHomeFromPantry)

        val items = mutableListOf<String>()
        val adapter = PantryListAdapter(this, items)
        pantryList.adapter = adapter

        // Check if the user is in a group
        val userRef = database.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("groupCode").exists()) {
                    // User is in a group, retrieve the group code
                    groupCode = dataSnapshot.child("groupCode").value.toString()
                    // Update the database reference to the group's pantry list
                    database = database.child("groups").child(groupCode).child("pantry")
                } else {
                    // User is not in a group, use the user's own pantry list
                    database = database.child("users").child(userId).child("pantry")
                }

                // Retrieve and display the items from the pantry list
                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        items.clear()
                        for (ds in dataSnapshot.children) {
                            val item = ds.getValue(String::class.java)
                            items.add(item!!)
                        }

                        if (items.isEmpty()) {
                            items.add("Add Items to Pantry")
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "userRef:onCancelled", databaseError.toException())
            }
        })

        // Adding an item to the pantry list
        addButton.setOnClickListener {
            val item = addItem.text.toString()
            database.push().setValue(item)
            addItem.text.clear()
            items.remove("Add Items to Pantry")
            Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show()
        }

        // Go back home button
        backHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Go to the grocery list
        val btnGroceryList = findViewById<Button>(R.id.btnGroceryList)
        btnGroceryList.setOnClickListener {
            startActivity(Intent(this, GroceryListActivity::class.java))
        }

        //-----------------
        // REMOVING ITEM FROM FIREBASE
        val userRef2 = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        userRef2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isInGroup = dataSnapshot.child("groupCode").exists()
                val databaseRef = if (isInGroup) {
                    // User is in a group
                    val groupCode = dataSnapshot.child("groupCode").value.toString()
                    FirebaseDatabase.getInstance().reference
                        .child("groups")
                        .child(groupCode)
                        .child("pantry")
                } else {
                    // User is not in a group, use user's pantry
                    FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(userId)
                        .child("pantry")
                }

                pantryList.setOnItemClickListener { _, _, position, _ ->
                    val clickedItem = adapter.getItem(position)

                    val builder = AlertDialog.Builder(this@MyPantryActivity)
                    builder.setTitle("Delete Item")
                    builder.setMessage("Are you sure you want to delete this item?")
                    builder.setPositiveButton("Delete") { _, _ ->
                        Toast.makeText(this@MyPantryActivity, "Item deleted!", Toast.LENGTH_SHORT).show()

                        val itemsQuery = databaseRef.orderByValue().equalTo(clickedItem)
                        itemsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (itemSnapshot in dataSnapshot.children) {
                                    itemSnapshot.ref.removeValue()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("Delete Item", "Failed to delete item", databaseError.toException())
                            }
                        })
                    }

                    builder.setNegativeButton("Cancel") { _, _ ->
                        Toast.makeText(this@MyPantryActivity, "Deletion canceled!", Toast.LENGTH_SHORT).show()
                    }

                    builder.create().show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "userRef:onCancelled", databaseError.toException())
            }
        })

        //----------------- END OF CLICK ON ITEM TO DELETE
    }
}

