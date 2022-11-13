package com.example.imgfb

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imgfb.databinding.ActivityMainBinding
import com.example.imgfb.databinding.ActivityStorageBinding
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class StorageActivity : AppCompatActivity() {
        lateinit var binding: ActivityStorageBinding
        //imageuri is second
        lateinit var ImageUri :Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImagBtn.setOnClickListener {
            //call method
            selectImage()
        }
        binding.uploadImageBtn.setOnClickListener {
            uploadImage()
            Toast.makeText(this, "uploading", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage() {
        //third step. whenever user clicks on upload image show progress bar
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Upload File...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat( "yyy_mm_dd_HHH_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.putFile(ImageUri).
                addOnSuccessListener {
                    binding.firebaseImage.setImageURI(null)
                    Toast.makeText(this@StorageActivity, "Successfully Uploaded", Toast.LENGTH_SHORT).show()
                    if(progressDialog.isShowing) progressDialog.dismiss()

                }.addOnFailureListener{
                    if (progressDialog.isShowing) progressDialog.dismiss()
                  Toast.makeText(this@StorageActivity, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)

    }
    //write method on activityresult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
             //after creating the img var
            ImageUri = data?.data!!
            binding.firebaseImage.setImageURI(ImageUri)
        }
    }
}
//when connecting to firebase, USE cloud storage firebase. Once connected in FB, open Storage under Build
//remember, once the app is launched, you select an image first, then upload to firebase.