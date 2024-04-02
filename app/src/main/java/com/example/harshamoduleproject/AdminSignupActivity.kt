package com.example.harshamoduleproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.harshamoduleproject.UserData
import com.example.harshamoduleproject.databinding.ActivityAdminSignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminSignupActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAdminSignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("admin")

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            val signupPhone = binding.SignupPhone.text.toString()
            val signupEmail = binding.signupEmail.text.toString()

            if(signupUsername.isNotEmpty() && signupPassword.isNotEmpty() && signupPhone.isNotEmpty() && signupEmail.isNotEmpty()){
                signupUser(signupUsername,signupPassword,signupPhone,signupEmail)
            } else{
                Toast.makeText(this@AdminSignupActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }

        }

//        binding.loginRedirectText.setOnClickListener {
//            Toast.makeText(this@AdminSignupActivity, "", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this@AdminSignupActivity, LoginActivity::class.java))
//            finish()
//        }

    }
    private fun signupUser(username: String, password: String, phone: String, email: String ){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.exists()){
                    val id = databaseReference.push().key
                    val userData = UserData(id, username, password,phone,email)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@AdminSignupActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AdminSignupActivity, AdminLoginActivity::class.java))
                    finish()
                } else{
                    Toast.makeText(this@AdminSignupActivity, "User alredy exists", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(databseError: DatabaseError) {
                Toast.makeText(this@AdminSignupActivity, "Database Error: ${databseError.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }
    // Override the onBackPressed method to handle the back button
    override fun onBackPressed() {
        // Add your custom logic here if needed
        super.onBackPressed()
    }
}