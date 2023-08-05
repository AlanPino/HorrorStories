package com.joeldev.horrorstories

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.joeldev.horrorstories.databinding.ActivityMainBinding
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var books = mutableListOf<Book>()

    private lateinit var booksAdapter: BooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBooks()
    }

    private fun getBooks() {
        val storageRef = FirebaseStorage.getInstance().reference

        storageRef.listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->
                books.add(Book(getName(item.name), getAuthor(item.name), "gs://horror-stories-de4b9.appspot.com/${item.name}", "story"))
                Log.i("x", item.downloadUrl.toString())
            }
        }.addOnSuccessListener {
            initUI()
        }
    }

    private fun initUI() {
        booksAdapter = BooksAdapter(books){onBookSelected(it)}
        binding.rvBooks.layoutManager = GridLayoutManager(this, 3)
        binding.rvBooks.adapter = booksAdapter
    }

    private fun onBookSelected(book: Book) {
        val intent = Intent(this, BookActivity::class.java)
        intent.putExtra("url", book.url)
        intent.putExtra("title", book.title)
        startActivity(intent)
    }

    private fun getName(line: String): String{
        return line.substring(0, line.lastIndexOf(",")).trim()
    }

    private fun getAuthor(line: String): String{
        return line.substring(line.lastIndexOf(",") + 2, line.length - 4).trim()
    }


}