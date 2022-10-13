package com.example.traderjoes20

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity

import com.example.traderjoes20.databinding.ActivityPantryBinding

class PantryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPantryBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}