package com.example.harshamoduleproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.harshamoduleproject.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding // Assuming you have a binding class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customerButton.setOnClickListener {
            Toast.makeText(this@IntroActivity, "Customer button clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
            finish()
        }

        binding.adminButton.setOnClickListener {
            Toast.makeText(this@IntroActivity, "Admin button clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@IntroActivity, AdminLoginActivity::class.java))
            finish()
        }
    }

    // Override the onBackPressed method to handle the back button
    override fun onBackPressed() {
        // Add your custom logic here if needed
        super.onBackPressed()
    }
}
