package com.example.health

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentsActivity : AppCompatActivity() {

    private lateinit var adapter: AppointmentAdapter
    private val appointmentList = mutableListOf<Appointment>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val btnBook = findViewById<Button>(R.id.btnBookAppointment)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerAppointments)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AppointmentAdapter(appointmentList)
        recyclerView.adapter = adapter

        btnBook.setOnClickListener {
            startActivity(Intent(this, BookAppointmentActivity::class.java))
        }

        loadAppointments()
    }

    private fun loadAppointments() {
        db.collection("users").document(userId!!)
            .collection("appointments")
            .get()
            .addOnSuccessListener { result ->
                appointmentList.clear()
                for (doc in result) {
                    val appointment = doc.toObject(Appointment::class.java)
                    appointmentList.add(appointment)
                }
                adapter.notifyDataSetChanged()
            }
    }
}
