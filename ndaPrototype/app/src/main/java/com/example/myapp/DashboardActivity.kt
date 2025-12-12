package com.example.myapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import android.widget.EditText
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.LinearLayout
import android.content.Context


class DashboardActivity : AppCompatActivity() {
    private val receivedList = mutableListOf<String>("from James Bilitski", "from Sandro", "from Stephan Ohl")
    private val activeList = mutableListOf<String>("from Bilal Hassan", "from Michael McGivern", "to Mustafa Abdalla")
    private val inactiveList = mutableListOf<String>("from Justin Rhodes", "from Jacob Shultz", "from Farell")

    private val pendingList = mutableListOf<String>()
    private var selectedIndex = 0;

    override fun onBackPressed() {
        // you shouldnt be able to go back to verification code from dashboard!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val passedFile = intent.getStringExtra("filename")
        if (passedFile != null) {
            pendingList.add(passedFile)
        }

        val create_nda_button: Button = findViewById(R.id.create_nda_button)
        create_nda_button.setOnClickListener {
            val intent = Intent(this, CreateNDAMainActivity::class.java)
            startActivity(intent)
        }

        val view_nda_button: Button = findViewById(R.id.view_nda_button)
        view_nda_button.setOnClickListener {
            showViewDialog()
        }

        val logout: ImageView = findViewById(R.id.logout)
        logout.setOnClickListener {
            // maybe implement a dialog asking for logout or cancel before logging out
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Logout") {dialog, which ->
                    Toast.makeText(this, "You've been successfully logged out", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        val inbox: ImageView = findViewById(R.id.inbox)
        inbox.setOnClickListener {
            showInboxDialog()
        }

        val viewAccount : ImageView = findViewById(R.id.viewAccount)
        viewAccount.setOnClickListener {
            showAccountDialog()
        }
    }

    fun showViewDialog() {
        selectedIndex = 0
        val context = this
        val spinner = Spinner(context)
        val options = listOf("Active", "Inactive", "Pending")

        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50,40,50,10)
        layout.addView(spinner)

        AlertDialog.Builder(this)
            .setTitle("Choose which NDAs you would like to view")
            .setView(layout)
            .setPositiveButton("Select") { _, _ ->
                val selected = spinner.selectedItem.toString()
                if (selected == "Active") {
                    showActiveDialog()
                } else if (selected == "Inactive"){
                    showInactiveDialog()
                } else {
                    showPendingDialog()
                }
            }
            .setNegativeButton("Cancel",null)
            .show()
    }

    fun showPendingDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Pending NDAs")
            .setCancelable(false)
        if (!pendingList.isEmpty()) {
            builder.setSingleChoiceItems(pendingList.toTypedArray(), selectedIndex) { _, which ->
                selectedIndex = which
            }
                .setPositiveButton("Select") { _, _ ->
                    showNdaPendingViewDialog(selectedIndex)
                }
        } else {
            builder.setView(TextView(this).apply {
                setText("You have no pending NDAs!")
                setPadding(80,50,80,40)

            })
        }
            .setNegativeButton("Back") {_,_ ->
                showViewDialog()
            }
        builder.show()
    }

    fun showNdaPendingViewDialog(selectedIndex : Int) {
        val selectBuilder = AlertDialog.Builder(this)
            .setTitle(pendingList[selectedIndex])
            .setCancelable(false)
            .setNeutralButton("Back") { _,_ ->
                showPendingDialog()
            }

        val ndaView = TextView(this)

        val fileName = pendingList[selectedIndex]

        lifecycleScope.launch {
            fileName.let { name ->
                val content = readInternalFile(this@DashboardActivity,name)
                content?.let { ndaView.text = it }
            }
        }

        ndaView.setPadding(40,20,40, 40)
        selectBuilder.setView(ndaView)
        selectBuilder.show()
    }

    fun showActiveDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Active NDAs")
            .setCancelable(false)
        if (!activeList.isEmpty()) {
            builder.setSingleChoiceItems(activeList.toTypedArray(), selectedIndex) {_, which ->
                selectedIndex = which
            }
                .setPositiveButton("Select") {_, _ ->
                    showNdaActiveViewDialog(selectedIndex)
                }
        } else {
            builder.setView(TextView(this).apply {
                setText("You have no active NDAs!")
                setPadding(80,50,80,40)
            })
        }
            .setNegativeButton("Back") {_,_ ->
                showViewDialog()
            }
        builder.show()
    }

    fun showAccountDialog() {
        val context : Context = this
        AlertDialog.Builder(this)
            .setTitle("Your Account")
            .setView(ImageView(this).apply {
                setImageDrawable(ContextCompat.getDrawable(context,R.drawable.sethbaker))
            })
            .setView(TextView(this).apply {
                setText("Seth Baker\n\n" +
                        "snb93@pitt.edu\n\n" +
                        "412-412-4120\n\n" +
                        "University of Pittsburgh at Johnstown")
                setPadding(100,50,80,40)
            })
            .show()
    }

    fun showInactiveDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Inactive NDAs")
            .setCancelable(false)
        if (!inactiveList.isEmpty()) {
            builder.setSingleChoiceItems(inactiveList.toTypedArray(), selectedIndex) {_, which ->
                selectedIndex = which
            }
                .setPositiveButton("Select") {_, _ ->
                    showNdaInactiveViewDialog(selectedIndex)
                }
        } else {
            builder.setView(TextView(this).apply {
                setText("You have no inactive NDAs!")
                setPadding(80,50,80,40)
            })
        }
            .setNegativeButton("Back") {_,_ ->
                showViewDialog()
            }
        builder.show()

    }

    private fun readInternalFile(context : Context, fileName: String) : String? {
        return try {
            context.openFileInput(fileName).bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun readAssetFile(fileName: String) : String? {
        return withContext(Dispatchers.IO) {
            try {
                applicationContext.assets.open(fileName).bufferedReader(Charsets.UTF_8).use {
                    it.readText()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
    fun showNdaActiveViewDialog(selectedIndex : Int) {
        val selectBuilder = AlertDialog.Builder(this)
            .setTitle(activeList[selectedIndex])
            .setCancelable(false)
            .setNeutralButton("Back") { _,_ ->
                showActiveDialog()
            }

        val ndaView = TextView(this)

        val fileName = when (activeList[selectedIndex]) {
            "from James Bilitski" -> "bilitski.txt"
            "from Sandro" -> "sandro.txt"
            "from Stephan Ohl" -> "ohl.txt"
            "from Bilal Hassan" -> "bilal.txt"
            "from Michael McGivern" -> "mcgivern.txt"
            "to Mustafa Abdalla" -> "abdalla.txt"
            else -> null
        }

        lifecycleScope.launch {
            fileName?.let { name ->
                val content = readAssetFile(name)
                content?.let { ndaView.text = it }
            }
        }

        ndaView.setPadding(40,20,40, 40)
        selectBuilder.setView(ndaView)
        selectBuilder.show()
    }

    fun showNdaInactiveViewDialog(selectedIndex : Int) {
        val selectBuilder = AlertDialog.Builder(this)
            .setTitle(inactiveList[selectedIndex])
            .setCancelable(false)
            .setNeutralButton("Back") { _,_ ->
                showInactiveDialog()
            }

        val ndaView = TextView(this)

        val fileName = when (inactiveList[selectedIndex]) {
            "from Justin Rhodes" -> "justin.txt"
            "from Jacob Shultz" -> "jacob.txt"
            "from Farell" -> "farell.txt"
            else -> null
        }

        lifecycleScope.launch {
            fileName?.let { name ->
                val content = readAssetFile(name)
                content?.let { ndaView.text = it }
            }
        }

        ndaView.setPadding(40,20,40, 40)
        selectBuilder.setView(ndaView)
        selectBuilder.show()
    }

    fun showInboxDialog() {

        val backIconDrawable = ContextCompat.getDrawable(this, R.drawable.go_back)

        val builder = AlertDialog.Builder(this)
            .setTitle("Your Inbox")
            .setCancelable(false)
            if (!receivedList.isEmpty()) {
                builder.setSingleChoiceItems(receivedList.toTypedArray(), 0) { _, which ->
                    selectedIndex = which
                }
                    .setPositiveButton("Select") { _, _ ->
                        showNdaSignDialog(selectedIndex)
                    }
            } else {
                builder.setView(TextView(this).apply {
                    setText("You have no remaining NDAs to sign or reject!")
                    setPadding(80,50,80,40)
                })
            }

            .setNegativeButton("", null)

        builder.setNegativeButtonIcon(backIconDrawable)
        builder.show()
    }

    fun showNdaRejectCommentDialog() {
        val commentInput = EditText(this)
        val builder = AlertDialog.Builder(this)
            .setTitle("Explain your rejection (this will be sent to initiator)")
            .setCancelable(false)
            .setView(commentInput)
            .setPositiveButton("send", null)

        val dialog = builder.create()

        dialog.setOnShowListener {
            val sendButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            sendButton.setOnClickListener {
                if (commentInput.text.toString() == "") {
                    commentInput.error = "You must leave some feedback to initiator"
                } else {
                    dialog.dismiss()
                    val rejectToast = Toast.makeText(this, "rejected NDA " + receivedList[selectedIndex], Toast.LENGTH_SHORT)
                    receivedList.removeAt(selectedIndex)
                    rejectToast.show()
                    showInboxDialog()
                }
            }
        }

        dialog.show()

    }

     fun showNdaSignDialog(selectedIndex : Int) {
        val selectBuilder = AlertDialog.Builder(this)
            .setTitle(receivedList[selectedIndex])
            .setCancelable(false)
            .setPositiveButton("sign") { _,_ ->
                showSignatureDialog(selectedIndex)
            }
            .setNeutralButton("reject") { _,_ ->
                showNdaRejectCommentDialog()
            }

         val ndaView = TextView(this)
         val fileName = when (receivedList[selectedIndex]) {
             "from James Bilitski" -> "bilitski.txt"
             "from Sandro" -> "sandro.txt"
             "from Stephan Ohl" -> "ohl.txt"
             else -> null
         }

         lifecycleScope.launch {
             fileName?.let { name ->
                 val content = readAssetFile(name)
                 content?.let { ndaView.text = it }
             }
         }

        ndaView.setPadding(40,20,40, 40)
        selectBuilder.setView(ndaView)
        selectBuilder.show()
    }

    fun showSignatureDialog(selectedIndex : Int) {
        val input = EditText(this).apply {
            hint = "Enter your full legal name"
        }
        val signatureDialog = AlertDialog.Builder(this)
            .setTitle("Confirm signature")
            .setMessage("Please enter your name to sign the NDA: ")
            .setView(input)
            .setPositiveButton("enter", null)
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .create()

        signatureDialog.setOnShowListener {
            val positiveButton = signatureDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = signatureDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setOnClickListener {
                val signature = input.text.toString().trim()
                if (signature.isEmpty()) {
                    input.error = "Signature cannot be empty!"
                } else {
                    val signedToast = Toast.makeText(
                        this,
                        "signed NDA " + receivedList[selectedIndex],
                        Toast.LENGTH_SHORT
                    )
                    activeList.add(receivedList.get(selectedIndex))
                    receivedList.removeAt(selectedIndex)
                    signedToast.show()
                    showInboxDialog()
                    signatureDialog.dismiss()
                }
            }
            negativeButton.setOnClickListener {
                Toast.makeText(this, "Signature action cancelled.", Toast.LENGTH_SHORT).show()
                showNdaSignDialog(selectedIndex)
                signatureDialog.dismiss()
            }
        }

        signatureDialog.show()
    }
}
