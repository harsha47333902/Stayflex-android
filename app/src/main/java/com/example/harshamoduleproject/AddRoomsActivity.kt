package com.example.harshamoduleproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.harshamoduleproject.databinding.ActivityAddRoomsBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.app.Activity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddRoomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRoomsBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Rooms") // Change to your data structure
        storageReference = FirebaseStorage.getInstance().reference.child("Rooms_images") // Change to your storage path

        binding.selectImageButton.setOnClickListener {
            openImageChooser()
        }

        binding.submitButton.setOnClickListener {
            val roomType = binding.homeactivity.text.toString()
            val price = binding.homecategory.text.toString()
            val totalRooms = binding.homeurl.text.toString()
            val description = binding.description.text.toString()

            if (roomType.isNotEmpty() && price.isNotEmpty() && totalRooms.isNotEmpty()) {
                if (selectedImageUri != null) {
                    uploadImageAndSaveData(roomType, price, totalRooms, description)
                } else {
                    Toast.makeText(this@AddRoomsActivity, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@AddRoomsActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImageAndSaveData(roomType: String, price: String, totalRooms: String , description: String) {
        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            val imageUrl = uriTask.result.toString()
                            saveRoomDataToDatabase(roomType, price, totalRooms, imageUrl, description)
                        } else {
                            Toast.makeText(this@AddRoomsActivity, "Image upload failed: ${uriTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@AddRoomsActivity, "Image upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveRoomDataToDatabase(roomType: String, price: String, totalRooms: String, imageUrl: String, description: String) {
        val id = databaseReference.push().key // Auto-incremental ID
        val roomData = AddRoomData(id, roomType, price, totalRooms, imageUrl, description)

        if (id != null) {
            databaseReference.child(id).setValue(roomData)
        }

        Toast.makeText(this@AddRoomsActivity, "Data entered into the database", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    // Override the onBackPressed method to handle the back button
    override fun onBackPressed() {
        // Add your custom logic here if needed
        super.onBackPressed()
    }
}

