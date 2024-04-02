package com.example.harshamoduleproject
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null

        // Get userId from Intent
        userId = intent.getStringExtra("userId") ?: ""

        // Set the HomeFragment as the initial fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.status -> replaceFragment(StatusFragment())
                R.id.tip -> replaceFragment(TipFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            true
        }

        // Initialize logout button
        val logoutButton = findViewById<ImageView>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Start LoginActivity when logout button is clicked
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish() // Close MainActivity
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("userId", userId)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.status -> {
                Toast.makeText(this, "My Products clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.tip -> {
                Toast.makeText(this, "Orders clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            android.R.id.home -> {
                onBackPressed() // Handle the back button click
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
