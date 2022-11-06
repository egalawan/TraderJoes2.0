package com.example.traderjoes20

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import android.widget.ArrayAdapter
import android.widget.ListView

import androidx.appcompat.app.AppCompatActivity

import com.example.traderjoes20.databinding.ActivityPantryBinding

class PantryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPantryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "Apple","Orange","Organic Banana","Water Melon", "Apricot", "Pineapple", "Mangos", "Campari Tomatoes", "Organic Strawberries 1 Lb ", "Strawberries 2 Lb ", "Strawberries 1 Lb", "Organic Bananas",
            "Herbs of provence", " Shishito Peppers ", "Shiitake Mushrooms ", " Organic Brussels Sprouts ", "Organic Sweet Potatoes", "Sweet Potato ", "Steamed Lentils\n", "Wild Arugula ",
            "Green Onions ", "Organic Carrots of Many Colors", "Organic Mini Sweet Peppers", "Organic Baby Spinach & Spring ", " Baby Cauliflower ", "Organic Petite Potato Medley", "Ready Veggies",
            "Baby Shanghai Bok Choy", " Riced Cauliflower ", " Russet Potatoes ", "Sugar Snap Peas", " Organic Red Bell Peppers ",  " Portabella Mushrooms ", "Organic Shredded Kale",
            "Organic Baby Lettuce Mix ", " Organic Arugula", " Corn on the Cob ",  "Super Sweet Fresh Corn ", "Broccoli & Kale Slaw ", "Organic Persian Cucumbers\n ",
            "Organic Shredded Green & Red ",
            )
        var mListView = findViewById<ListView>(R.id.userlist)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
        mListView.adapter = arrayAdapter

    }
}