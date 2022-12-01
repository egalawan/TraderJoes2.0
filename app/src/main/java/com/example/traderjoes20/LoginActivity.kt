package com.example.traderjoes20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonToRegister = findViewById<Button>(R.id.btnRegister)
        buttonToRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val buttonToMain = findViewById<Button>(R.id.btnContinue)
        buttonToMain.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}