package com.example.traderjoes20

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.databinding.CellBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.HashMap
import com.squareup.picasso.Picasso

// with shopActivity
class RVAdapter(
    private val cell: ArrayList<Cell>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemName = ArrayList<String>()
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    class ItemViewHolder(var viewBinding: CellBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //for shop page
        val itemViewHolder = holder as ItemViewHolder
        itemViewHolder.viewBinding.itemIdTextview.text = cell[position].itemId
        itemViewHolder.viewBinding.itemNameTextview.text = cell[position].itemName
        itemViewHolder.viewBinding.itemPriceTextview.text = cell[position].itemPrice
        itemViewHolder.viewBinding.itemWeightTextview.text = cell[position].itemWeight
        //val itemName = cell[position].itemName
        Picasso.get().load(cell[position].itemUrl).into(itemViewHolder.viewBinding.imageView3)


        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference

        itemViewHolder.viewBinding.btnAddtoPantry.setOnClickListener {
            val shopItem = cell[position].itemName

            // Check if the user is in a group
            val userRef = database.child("users").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val databaseReference: DatabaseReference = if (dataSnapshot.child("groupCode").exists()) {
                        // User is in a group, retrieve the group code
                        val groupCode = dataSnapshot.child("groupCode").value.toString()
                        // Update the database reference to the group's pantry
                        database.child("groups").child(groupCode).child("pantry")
                    } else {
                        // User is not in a group, use the user's own pantry
                        database.child("users").child(userId).child("pantry")
                    }

                    // Use the shopItem as the value of the pantry item
                    databaseReference.push().setValue(shopItem)
                        .addOnSuccessListener {

                            Toast.makeText(itemViewHolder.viewBinding.root.context, "Item added to pantry!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(itemViewHolder.viewBinding.root.context, "Failed to add item to pantry: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "userRef:onCancelled", databaseError.toException())
                }
            })
        }

    }

    override fun getItemCount(): Int {
        return cell.size
    }
    fun getFilteredRecipeList(): ArrayList<String> {
        return itemName
    }
}