package com.example.traderjoes20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traderjoes20.Models.Recipe
import com.example.traderjoes20.databinding.ActivityRecipesBinding
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class RecipesActivity : AppCompatActivity() {

    var itemsArray: ArrayList<Recipe> = ArrayList()
    lateinit var adapter: RecipesAdapter

    private lateinit var binding: ActivityRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.RecipesRecyclerview.layoutManager = layoutManager
        binding.RecipesRecyclerview.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(
                binding.RecipesRecyclerview.context,
                layoutManager.orientation
            )
        ContextCompat.getDrawable(this, R.drawable.line_divider)
            ?.let { drawable -> dividerItemDecoration.setDrawable(drawable) }
        binding.RecipesRecyclerview.addItemDecoration(dividerItemDecoration)
    }
}
