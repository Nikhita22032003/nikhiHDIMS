package com.example.health

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.health.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Fetch user data from Firestore
        firestore.collection("users").document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name") ?: ""
                    val email = currentUser.email ?: ""
                    val dob = document.getString("dob") ?: ""
                    val gender = document.getString("gender") ?: ""
                    val bloodGroup = document.getString("bloodGroup") ?: ""
                    val phone = document.getString("phone") ?: ""
                    val address = document.getString("address") ?: ""
                    val allergies = document.getString("allergies") ?: "None"
                    val chronic = document.getString("chronicConditions") ?: "None"
                    val emergency = document.getString("emergencyContact") ?: "Not Provided"

                    // Set user info in UI
                    binding.nameText.text = "Hi, $name"
                    binding.emailText.text = "Email: $email"
                    binding.dobText.text = "DOB: $dob"
                    binding.genderText.text = "Gender: $gender"
                    binding.bloodGroupText.text = "Blood Group: $bloodGroup"
                    binding.allergiesText.text = "Allergies: $allergies"
                    binding.chronicText.text = "Chronic: $chronic"
                    binding.emergencyContactText.text = "Emergency Contact: $emergency"

                    // Optional combined info display
                    binding.tvDetails.text = """
                        Phone: $phone
                        Address: $address
                    """.trimIndent()
                } else {
                    binding.nameText.text = "Welcome!"
                    binding.tvDetails.text = "User data not found!"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }

        // Logout button
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnHealthRecords).setOnClickListener {
            startActivity(Intent(this, HealthRecordsActivity::class.java))
        }
        findViewById<Button>(R.id.btnAppointments).setOnClickListener {
            startActivity(Intent(this, AppointmentsActivity::class.java))
        }
        findViewById<Button>(R.id.btnMedicalHistory).setOnClickListener {
            startActivity(Intent(this, MedicalHistoryActivity::class.java))
        }
        findViewById<Button>(R.id.btnEmergencyInfo).setOnClickListener {
            startActivity(Intent(this, EmergencyInfoActivity::class.java))
        }
        // Future activity navigation (placeholders)
        binding.btnUploadRecords.setOnClickListener {
            Toast.makeText(this, "Upload screen coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnViewRecords.setOnClickListener {
            Toast.makeText(this, "View records screen coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnViewAppointments.setOnClickListener {
            Toast.makeText(this, "Upcoming appointments screen coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnBookAppointment.setOnClickListener {
            Toast.makeText(this, "Booking screen coming soon", Toast.LENGTH_SHORT).show()
        }
    }
}
