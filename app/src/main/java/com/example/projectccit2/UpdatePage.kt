package com.example.projectccit2

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdatePage : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase
                .getInstance("https://projectccit-7f186-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users")

        val search = findViewById<Button>(R.id.buttonCari)
        val update = findViewById<Button>(R.id.updateDataButton)
        val kembali = findViewById<Button>(R.id.backButtonEdit)
        val tgl_lahir = findViewById<Button>(R.id.buttonTglEdit)
        search.setOnClickListener {
            pencarian()
        }

        update.setOnClickListener {
            updating()
        }

        kembali.setOnClickListener {
            startActivity(Intent(this, DashboardPage::class.java))
        }

        tgl_lahir.setOnClickListener {
            getDate(tgl_lahir)
        }
    }

    private fun pencarian() {
        val induk = findViewById<EditText>(R.id.inputNik)
        val nama = findViewById<EditText>(R.id.editNama)
        val tanggal = findViewById<EditText>(R.id.editTgl)
        val tempat = findViewById<EditText>(R.id.editTmpt)
        val genderLaki = findViewById<RadioButton>(R.id.radioButtonLakiEdit)
        val genderPerempuan = findViewById<RadioButton>(R.id.radioButtonPerempuanEdit)
        val status = findViewById<Spinner>(R.id.spinnerStatusEdit)
        val agama = findViewById<Spinner>(R.id.spinnerAgamaEdit)
        var nik = induk.text.toString().trim()

        if (nik.isNotEmpty()) {
            var query = reference.orderByChild("nik").equalTo(nik)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            nama.setText(userSnapshot.child("nama").getValue().toString())
                            tanggal.setText(userSnapshot.child("tanggal_lahir").getValue().toString())
                            tempat.setText(userSnapshot.child("tempat_lahir").getValue().toString())

                        if(userSnapshot.child("gender").getValue().toString() == "Laki Laki") {
                            Toast.makeText(baseContext, userSnapshot.child("gender").getValue().toString(), Toast.LENGTH_LONG).show()
                            genderLaki.isChecked = true
                        }else if(userSnapshot.child("gender").getValue().toString() == "Perempuan"){
                            genderPerempuan.isChecked= true
                        }
                            for (i in 0..3) {
                                if (status.getItemAtPosition(i) == userSnapshot.child("status").getValue().toString()) {
                                    status.setSelection(i)
                                    return
                                }
                            }
                            for (i in 0..(agama.count)) {
                                if (agama.getItemAtPosition(i) == userSnapshot.child("status").getValue().toString()) {
                                    agama.setSelection(i)
                                    return
                                }
                            }
                        }
                    } else {
                        Toast.makeText(baseContext, "Data tidak ditemukan!", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(baseContext, "Data tidak ditemukan!", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updating() {
        val induk = findViewById<EditText>(R.id.inputNik).text.toString().trim()
        val nama = findViewById<EditText>(R.id.editNama).text.toString().trim()
        val tanggal = findViewById<EditText>(R.id.editTgl).text.toString().trim()
        val tempat = findViewById<EditText>(R.id.editTmpt).text.toString().trim()
        val status = findViewById<Spinner>(R.id.spinnerStatusEdit).selectedItem.toString()
        val agama = findViewById<Spinner>(R.id.spinnerAgamaEdit).selectedItem.toString()
        var radiogender = findViewById<RadioGroup>(R.id.radioGroupEdit)
        var date_edited = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        var currentUser = auth.currentUser.uid.toString()

        radiogender.setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
            val gender = rb.text.toString()
            var query = reference.orderByChild("nik").equalTo(induk)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val updates = hashMapOf<String, Any?>(
                                    "nama" to nama,
                                    "tempat_lahir" to tempat,
                                    "tanggal_lahir" to tanggal,
                                    "gender" to gender,
                                    "status" to status,
                                    "agama" to agama,
                                    "date_edited" to date_edited,
                                    "edited_by" to currentUser
                            )
                            userSnapshot.ref.updateChildren(updates)

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(baseContext, "Error!", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(tglLahir: Button) {
        val c = Calendar.getInstance()
        val date = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        val tglLahirForm = findViewById<EditText>(R.id.editTgl)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, date, month, year ->
            tglLahirForm.setText("$date-$month-$year")
        }, date, month, year)
        dpd.show()
    }

    private fun statusSpiner() {
        val statusView = findViewById<Spinner>(R.id.spinnerStatusEdit)

    }
}


