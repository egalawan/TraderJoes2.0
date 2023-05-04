package com.example.traderjoes20

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traderjoes20.databinding.CellBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

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
            database = FirebaseDatabase.getInstance().reference
                .child("users")
                .child(userId)
                .child("pantry")
            database.push().setValue(shopItem)

        }
    }

    override fun getItemCount(): Int {
        return cell.size
    }
    fun getFilteredRecipeList(): ArrayList<String> {
        return itemName
    }
}