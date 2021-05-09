package com.example.projectccit2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardPage : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_dashboard_page)
        val add = findViewById<Button>(R.id.tambahDataButton)
        val edit = findViewById<Button>(R.id.editDataButton)
        val read = findViewById<Button>(R.id.lihatDataButton)
        val delete = findViewById<Button>(R.id.hapusDataButton)
        val logout = findViewById<Button>(R.id.logoutButton)

        add.setOnClickListener {
            startActivity(Intent(this, AddPage::class.java))
        }

        edit.setOnClickListener {
            startActivity(Intent(this, UpdatePage::class.java))
        }

        read.setOnClickListener {
            startActivity(Intent(this, ReadPage::class.java))
        }

        delete.setOnClickListener {
            startActivity(Intent(this, DeletePage::class.java))
        }

        logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }
        }


}
