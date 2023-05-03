package com.example.traderjoes20
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.traderjoes20.R.id.backToLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import com.google.firebase.database.ktx.database

@DelicateCoroutinesApi
class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //back button
        val buttonToLogin = findViewById<Button>(backToLogin)
        buttonToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //end of button

        //register btn to create acc
        val buttonRegisterCreateAccount = findViewById<Button>(R.id.btnCreateAccount)

        buttonRegisterCreateAccount.setOnClickListener{
            performSignUp()
            //  val intent = Intent(this, RegisterActivity::class.java)
            // startActivity(intent)
        }

    }
    private fun performSignUp() {
        val name = findViewById<EditText>(R.id.editTextUsername)
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.reg_password)

        if (name.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        val inputName = name.text.toString()
        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val user = hashMapOf(
                            "fullName" to inputName,
                            "email" to inputEmail
                        )

                        // Use the database reference instead of Firestore
                        val database = Firebase.database
                        database.reference.child("users").child(userId).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "User info saved", Toast.LENGTH_SHORT).show()

                                // Move to the HomeActivity after successfully saving the user's information
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)

                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error saving user info: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    val exception = task.exception
                    Toast.makeText(
                        baseContext, "Authentication failed: ${exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }




}