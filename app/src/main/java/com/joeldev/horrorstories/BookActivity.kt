package com.joeldev.horrorstories

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.core.view.isVisible
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.joeldev.horrorstories.databinding.ActivityBookBinding
import java.io.File
import java.io.IOException

class BookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookBinding
    private lateinit var title: String

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")!!
        title = intent.getStringExtra("title")!!

        if(File(cacheDir, "${title}.pdf").exists()){
            displayPdfPage()
        }
        else{
            downloadFileFromFirebaseStorage(url)
        }

        initListeners()
    }

    private fun initListeners() {
        binding.next.setOnClickListener {
            page++
            displayPdfPage()
        }

        binding.before.setOnClickListener {
            page--
            displayPdfPage()
        }
    }

    private fun downloadFileFromFirebaseStorage(fileUrl: String) {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl)
        storageRef.getFile(File(cacheDir, "${title}.pdf")).addOnSuccessListener { displayPdfPage() }
    }

    private fun displayPdfPage() {
        try {
            val parcelFileDescriptor: ParcelFileDescriptor = ParcelFileDescriptor.open(File(cacheDir, "${title}.pdf"), ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            val page = pdfRenderer.openPage(page)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            binding.imgPage.setImageBitmap(bitmap)

            binding.next.isVisible = pdfRenderer.pageCount != this.page + 1
            binding.before.isVisible = this.page != 0

            binding.pageCount.text = "${this.page + 1}/${pdfRenderer.pageCount}"

            page.close()
            pdfRenderer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}