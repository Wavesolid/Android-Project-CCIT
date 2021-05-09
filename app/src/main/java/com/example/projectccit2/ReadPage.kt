package com.example.projectccit2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ReadPage : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    private lateinit var query : Query
    var nik =  Intent().getStringExtra("NIK")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_page)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase
                    .getInstance("https://projectccit-7f186-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users")
        val induk = findViewById<TextView>(R.id.inputNikShow)


        induk.setText(nik)

        val cariButton = findViewById<Button>(R.id.buttonCariShow)
        val kembali = findViewById<Button>(R.id.buttonKembaliShow)

        cariButton.setOnClickListener {
            searching()
        }

        kembali.setOnClickListener {
            startActivity(Intent(this, DashboardPage::class.java))
        }
    }

    private fun searching() {
        val inputNik = findViewById<EditText>(R.id.inputNikShow)
        val viewData = findViewById<TextView>(R.id.textViewShowData)
        var nik = inputNik.text.toString().trim()

        if(nik.isNotEmpty()) {
            query = reference.orderByChild("nik").equalTo(nik)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        for (userSnapshot in dataSnapshot.children) {
                            viewData.textSize = 20F
                            viewData.text ="Nama Lengkap: "+userSnapshot.child("nama").getValue().toString() + "\n\n" +
                                    "NIK: " + userSnapshot.child("nik").getValue().toString() + "\n\n" +
                                    "Tempat Lahir: "+ userSnapshot.child("tempat_lahir").getValue().toString() + "\n\n" +
                                    "Tamggal Lahir: " + userSnapshot.child("tanggal_lahir").getValue().toString() + "\n\n" +
                                    "Kelamin: " + userSnapshot.child("gender").getValue().toString() + "\n\n" +
                                    "Status: " + userSnapshot.child("status").getValue().toString() + "\n\n" +
                                    "Agama: "+ userSnapshot.child("agama").getValue().toString() + "\n\n"
                        }
                    }else{
                        viewData.setText("")
                        Toast.makeText(baseContext, "Data tidak ditemukan!", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(baseContext, "Data tidak ditemukan!", Toast.LENGTH_LONG).show()
                }
            })

            }
        else {
            Toast.makeText(baseContext, "Harap isi NIK!", Toast.LENGTH_LONG).show()
        }
    }
}


