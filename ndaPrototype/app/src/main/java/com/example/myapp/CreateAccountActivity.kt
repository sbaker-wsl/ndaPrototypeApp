package com.example.myapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val createAccount : Button = findViewById(R.id.createAccount)
        createAccount.setOnClickListener {
            val fName : EditText = findViewById(R.id.firstName)
            val lName : EditText = findViewById(R.id.lastName)
            val dob : EditText = findViewById(R.id.dateOfBirth)
            val phone : EditText = findViewById(R.id.phoneNumber)
            val company : EditText = findViewById(R.id.companyName)
            val email : EditText = findViewById(R.id.email)
            if (fName.text.toString() == "" || lName.text.toString() == "" || dob.text.toString() == "" || phone.text.toString() == "" || company.text.toString() == "" || email.text.toString() == "") {
                Toast.makeText(this, "You must fill in all fields!", Toast.LENGTH_SHORT).show()
            } else {
                val emailText = email.text.toString()
                Toast.makeText(this, "Account created for $emailText!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val back : Button = findViewById(R.id.backButton)
        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}