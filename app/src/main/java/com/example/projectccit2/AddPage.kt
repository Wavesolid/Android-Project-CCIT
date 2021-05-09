package com.example.projectccit2

import android.annotation.SuppressLint
import android.content.Intent
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

    class AddPage : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
//    var gender = ""
        @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_data)
        database = FirebaseDatabase
                    .getInstance("https://projectccit-7f186-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Users")
        auth = FirebaseAuth.getInstance()

        val daftar = findViewById<Button>(R.id.buttonDaftar)
        val kembali = findViewById<Button>(R.id.buttonKembaliDaftar)
        val tglLahir = findViewById<Button>(R.id.buttonTglDaftar)

        daftar.setOnClickListener {
            pendaftaran()
        }

        kembali.setOnClickListener {
            startActivity(Intent(this, DashboardPage::class.java))
        }

        tglLahir.setOnClickListener {
            getDate(tglLahir)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun pendaftaran( ) {
        val namaForm = findViewById<EditText>(R.id.inputNama)
        val tglLahirForm = findViewById<EditText>(R.id.inputTglLahir)
        val tmpLahir = findViewById<EditText>(R.id.inputTmptLahir)
        val statusSpinner = findViewById<Spinner>(R.id.spinnerStatusDaftar)
        val agamaSpinner = findViewById<Spinner>(R.id.spinnerAgamaDaftar)
        val radiogender = findViewById<RadioGroup>(R.id.radioGroupDaftar)

        var userRegister =auth.currentUser?.uid.toString()
        var nama = namaForm.text.toString()
        var tempat = tmpLahir.text.toString()
        var nik = getRandomNumberString()
        var status = statusSpinner.selectedItem.toString()
        var agama = agamaSpinner.selectedItem.toString()
        var tanggal = tglLahirForm.text.toString()
        var currentDateTime = LocalDateTime.now()
        var dateTimeFixed = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

        if(nama.isNotEmpty()&& tanggal.isNotEmpty() && tempat.isNotEmpty() && status.isNotEmpty() && agama.isNotEmpty()) {

            radiogender.setOnCheckedChangeListener { group, checkedId ->
                val rb = findViewById<View>(checkedId) as RadioButton
                val gender = rb.text.toString()
                Toast.makeText(baseContext, gender, Toast.LENGTH_LONG).show()
            }
                var model = DatabaseModel(nik, nama, tanggal,tempat, "this.gender" , status, agama, userRegister, dateTimeFixed)
                var id = reference.push().key
                reference.child(id!!).setValue(model)
                Toast.makeText(baseContext, "Akun berhasil dibuat!", Toast.LENGTH_LONG).show()
//                val intent = Intent(baseContext, UpdatePage::class.java)
//                intent.putExtra("NIK", nik)
//                startActivity(intent)
//                finish()

            }else {
            Toast.makeText(baseContext, "Akun tidak dapat dibuat!", Toast.LENGTH_LONG).show()
        }

     }

    fun getRandomNumberString(): String? {
        // It will generate 16 digit random Number.
        // from 0 to 9999999999999999
        var rnd = Random
        var number = rnd.nextLong(9999999999999999)

        // this will convert any number sequence into 16 character.
        return String.format("%16d", number)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDate(tglLahir: Button) {
        val c = Calendar.getInstance()
        val date = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        val tglLahirForm = findViewById<EditText>(R.id.inputTglLahir)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, date, month, year ->
                tglLahirForm.setText("$date-$month-$year")
            }, date, month, year)
            dpd.show()

    }

}

