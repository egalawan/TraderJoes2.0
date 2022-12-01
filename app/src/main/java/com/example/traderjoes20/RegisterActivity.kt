package com.example.traderjoes20
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.traderjoes20.R.id.backToLogin
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //back button
        val buttonToLogin = findViewById<Button>(backToLogin)
        buttonToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //end of button
    }
}