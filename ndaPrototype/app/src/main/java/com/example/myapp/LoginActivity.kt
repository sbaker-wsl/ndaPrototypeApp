package com.example.myapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginmain)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_button)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val get_code_button : Button = findViewById(R.id.get_code_button)
        get_code_button.setOnClickListener {
            val email = findViewById<EditText>(R.id.login_email).text.toString()
            Toast.makeText(this, "Verification code sent to $email", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VerificationActivity::class.java)
            startActivity(intent)
        }
        val createAccountButton : Button = findViewById(R.id.createAccountButton)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }
}