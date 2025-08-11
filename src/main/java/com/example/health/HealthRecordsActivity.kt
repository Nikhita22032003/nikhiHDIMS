package com.example.health

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class HealthRecordsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DocumentAdapter
    private val documentList = mutableListOf<String>()

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    private val PICK_FILE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_records)

        val btnUpload = findViewById<Button>(R.id.btnUploadDocument)
        recyclerView = findViewById(R.id.recyclerViewDocuments)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DocumentAdapter(documentList)
        recyclerView.adapter = adapter

        btnUpload.setOnClickListener {
            pickFileFromStorage()
        }

        loadDocuments()
    }

    private fun pickFileFromStorage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data
            fileUri?.let { uploadFileToFirebase(it) }
        }
    }

    private fun uploadFileToFirebase(uri: Uri) {
        val filename = System.currentTimeMillis().toString() + "_" + uri.lastPathSegment
        val fileRef = storage.reference.child("health_records/$userId/$filename")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    db.collection("users").document(userId!!)
                        .collection("documents")
                        .add(mapOf("url" to downloadUrl.toString()))
                        .addOnSuccessListener {
                            Toast.makeText(this, "File uploaded!", Toast.LENGTH_SHORT).show()
                            documentList.add(downloadUrl.toString())
                            adapter.notifyDataSetChanged()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadDocuments() {
        db.collection("users").document(userId!!)
            .collection("documents").get()
            .addOnSuccessListener { result ->
                documentList.clear()
                for (doc in result) {
                    doc.getString("url")?.let { documentList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
    }
}
