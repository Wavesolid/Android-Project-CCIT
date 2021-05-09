package com.example.projectccit2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DeletePage : AppCompatActivity() {
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_page)
        val search = findViewById<Button>(R.id.buttonCariHapus)
        val hapus = findViewById<Button>(R.id.buttonHapus)
        val kembali = findViewById<Button>(R.id.buttonKembaliHapus)

        database = FirebaseDatabase
                .getInstance("https://projectccit-7f186-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users")

        search.setOnClickListener {
            pencarian()
        }

        hapus.setOnClickListener {
            penghapusan()
        }

        kembali.setOnClickListener {
            startActivity(Intent(this, DashboardPage::class.java))
        }

    }

    private fun pencarian() {
        val inputNik = findViewById<EditText>(R.id.inputNikHapus)
        val viewData = findViewById<TextView>(R.id.textViewHapus)
        var nik = inputNik.text.toString().trim()

        if(nik.isNotEmpty()) {
           var query = reference.orderByChild("nik").equalTo(nik)
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

    private fun penghapusan() {
        val induk = findViewById<EditText>(R.id.inputNikHapus)
        val nik = induk.text.toString().trim()
        var query = reference.orderByChild("nik").equalTo(nik)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        userSnapshot.ref.removeValue()
                    }
                    induk.setText("")
                    Toast.makeText(baseContext, "Data berhasil dihapus", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(baseContext, "Data Tidak dapat dihapus", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Error!", Toast.LENGTH_LONG).show()
            }
        })
}
}