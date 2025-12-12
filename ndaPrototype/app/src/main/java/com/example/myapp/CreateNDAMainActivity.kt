package com.example.myapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import android.widget.Toast
import android.util.Patterns
import java.io.FileNotFoundException
import java.io.IOException
import android.content.Context

class CreateNDAMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_ndamain)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val create_nda_button : Button = findViewById(R.id.create_nda_button)
        create_nda_button.setOnClickListener {
            val builder = AlertDialog.Builder(this);
            builder.setTitle("Enter NDA filename")
            val input = EditText(this);
            input.hint = "filename.txt"
            builder.setView(input)

            builder.setPositiveButton("OK", null)
            builder.setNegativeButton("Cancel") {dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()

            dialog.setOnShowListener {
                val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                okButton.setOnClickListener {
                    val filename = input.text.toString();
                    if (!filename.endsWith(".txt")) {
                        input.error = "Filename must end with .txt"
                    }
                    else {
                        Toast.makeText(this, "NDA created successfully!", Toast.LENGTH_SHORT).show()
                        val contents : EditText = findViewById(R.id.nda_text)
                        writeFile(filename, contents.text.toString())
                        // email dialog
                        val emailBuilder = AlertDialog.Builder(this)
                        emailBuilder.setTitle("Enter the email of the recipient")
                        emailBuilder.setCancelable(false)
                        val emailInput = EditText(this)
                        emailInput.hint = "demo@example.com"
                        emailBuilder.setView(emailInput)
                        emailBuilder.setPositiveButton("Send", null)

                        val emailDialog = emailBuilder.create()

                        emailDialog.setOnShowListener {
                            val sendButton = emailDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            sendButton.setOnClickListener {
                                val enteredEmail = emailInput.text.toString()
                                if (Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
                                    Toast.makeText(this, "Sent NDA to $enteredEmail!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, DashboardActivity::class.java)
                                    intent.putExtra("filename", filename)
                                    startActivity(intent)
                                    emailDialog.dismiss()
                                    dialog.dismiss()
                                } else {
                                    emailInput.error ="Must enter a valid email!"
                                }
                            }
                        }

                        emailDialog.show()
                    }
                }
            }
            dialog.show()
        }
    }

    private fun writeFile(fileName: String, content: String) {
        try {
            openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(content.toByteArray())
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "IO Exception occurred", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "IOException occurred", Toast.LENGTH_SHORT).show()
        }
    }
}