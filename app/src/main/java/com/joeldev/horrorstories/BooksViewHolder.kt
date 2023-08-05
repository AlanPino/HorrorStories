package com.joeldev.horrorstories


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.joeldev.horrorstories.databinding.BookItemBinding

class BooksViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = BookItemBinding.bind(view)
    fun bind(book: Book){
        binding.titleBook.text = book.title
        binding.authorBook.text = book.author
    }
}