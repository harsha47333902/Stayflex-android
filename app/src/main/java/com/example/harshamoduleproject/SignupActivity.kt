package com.example.harshamoduleproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.harshamoduleproject.UserData
import com.example.harshamoduleproject.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            val signupPhone = binding.SignupPhone.text.toString()
            val signupEmail = binding.signupEmail.text.toString()

            if (signupUsername.isNotEmpty() && signupPassword.isNotEmpty() && signupPhone.isNotEmpty() && signupEmail.isNotEmpty()) {
                signupUser(signupUsername, signupPassword, signupPhone, signupEmail)
            } else {
                Toast.makeText(this@SignupActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun signupUser(username: String, password: String, phone: String, email: String) {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val id = databaseReference.push().key
                    val userData = UserData(id, username, password, phone, email)
                    if (id != null) {
                        databaseReference.child(id).setValue(userData)
                        Toast.makeText(this@SignupActivity, "Signup Successful", Toast.LENGTH_SHORT).show()

                        // Pass user ID to LoginActivity
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        intent.putExtra("USER_ID", id) // Pass user ID as extra
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "User already exists", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignupActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
