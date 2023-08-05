package com.joeldev.horrorstories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BooksAdapter(var books: MutableList<Book>, private val onBookSelected: (Book) -> Unit) : RecyclerView.Adapter<BooksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BooksViewHolder(layoutInflater.inflate(R.layout.book_item, parent, false))
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        holder.bind(books[position])

        holder.itemView.setOnClickListener {
            onBookSelected(books[position])
        }
    }
}