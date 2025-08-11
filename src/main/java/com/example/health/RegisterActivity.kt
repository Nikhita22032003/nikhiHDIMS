package com.example.health

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Firebase init
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get views
        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val dob = findViewById<EditText>(R.id.etDob)
        val gender = findViewById<EditText>(R.id.etGender)
        val bloodGroup = findViewById<EditText>(R.id.etBloodGroup)
        val phone = findViewById<EditText>(R.id.etPhone)
        val address = findViewById<EditText>(R.id.etAddress)
        val allergies = findViewById<EditText>(R.id.etAllergies)
        val chronic = findViewById<EditText>(R.id.etChronic)
        val emergency = findViewById<EditText>(R.id.etEmergencyContact)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Email & Password are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Step 1: Create user in Firebase Auth
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid

                        // Step 2: Create user info map
                        val userMap = hashMapOf(
                            "name" to name.text.toString(),
                            "email" to userEmail,
                            "dob" to dob.text.toString(),
                            "gender" to gender.text.toString(),
                            "bloodGroup" to bloodGroup.text.toString(),
                            "phone" to phone.text.toString(),
                            "address" to address.text.toString(),
                            "allergies" to allergies.text.toString(),
                            "chronic" to chronic.text.toString(),
                            "emergencyContact" to emergency.text.toString()
                        )

                        // Step 3: Save to Firestore
                        if (userId != null) {
                            db.collection("users").document(userId).set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, DashboardActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
}
