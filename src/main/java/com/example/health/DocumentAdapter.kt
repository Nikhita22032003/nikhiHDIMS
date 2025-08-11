package com.example.health

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DocumentAdapter(private val documents: List<String>) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val docName: TextView = itemView.findViewById(R.id.textViewDocName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val docUrl = documents[position]
        val filename = Uri.parse(docUrl).lastPathSegment ?: "Document"
        holder.docName.text = filename

        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(docUrl))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = documents.size
}
