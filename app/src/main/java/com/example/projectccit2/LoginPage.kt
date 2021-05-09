package com.example.projectccit2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class LoginPage : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        
        val login = findViewById<Button>(R.id.buttonLogin)
        val signUp = findViewById<Button>(R.id.buttonSignUp)
        val resetPass = findViewById<Button>(R.id.resetPasswordButton)

        login.setOnClickListener {
            loginUser()
        }
        signUp.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
            finish()
        }
        resetPass.setOnClickListener {
            resetPassword()
        }
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.emailField)
        val password = findViewById<EditText>(R.id.passwordField)

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

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUI(null)
                    }
                }
    }

    private fun resetPassword() {
        val email = findViewById<EditText>(R.id.emailField)
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

        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Check your Email", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(baseContext, "Account Unavailable", Toast.LENGTH_LONG).show()
                }
            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            if(currentUser.isEmailVerified) {
                Toast.makeText(baseContext, "Login Successful", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, DashboardPage::class.java))
            }
            else {
                Toast.makeText(baseContext, "Please verify your account!", Toast.LENGTH_LONG).show()
            }
        }
        else {
            Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_LONG).show()
       }
    }
}