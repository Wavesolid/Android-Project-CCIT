package com.example.projectccit2

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterPage : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_register_page)

            val regis = findViewById<Button>(R.id.registerButton)
            auth = FirebaseAuth.getInstance()

            regis.setOnClickListener {
                registerUser()
            }
    }

    private fun registerUser() {
        val email = findViewById<EditText>(R.id.emailRegisField)
        val password = findViewById<EditText>(R.id.passwordRegisField)

        if(email.text.toString().isEmpty()) {
            email.error = "Please fill out the Email!"
            email.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter a valid Email"
            email.requestFocus()
            return
        }
        if(password.text.toString().isEmpty()) {
            password.error = "Please fill out the Password"
            password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(baseContext, "Please check your Email!", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, LoginPage::class.java))
                                    finish()
                                }
                            }
                } else {
                    onSnackFailed()
                }
            }
    }

    private fun onSnackFailed() {
        val snackbar = Snackbar.make(findViewById(R.id.registerLayout), "Failed to create the Account!",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)

        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)

        val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 20f
        snackbar.show()
    }
}