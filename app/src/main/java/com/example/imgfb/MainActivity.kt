package com.example.imgfb

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imgfb.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File
   //remember, before starting to code, open build.gradle and add buildfeatures
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //this is second last part
        binding.getImage.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Fetching image...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            //here
            val imageName = binding.etImageId.text.toString()
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName.jpg")

            val localfile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localfile).addOnSuccessListener {
                if(progressDialog.isShowing)
                    progressDialog.dismiss()
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageview.setImageBitmap(bitmap)
            }.addOnFailureListener{
                if(progressDialog.isShowing)
                    progressDialog.dismiss()
                Toast.makeText(this, "failed to retrieve image", Toast.LENGTH_SHORT).show()
            }

            var  i = Intent(this,StorageActivity::class.java)
            startActivity(i)
        }
    }
}