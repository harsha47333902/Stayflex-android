package com.example.harshamoduleproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null

        // Set the HomeFragment as the initial fragment
        if (savedInstanceState == null) {
            replaceFragment(AdminHomeFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.adminhome -> replaceFragment(AdminHomeFragment())
                R.id.adminreservation -> replaceFragment(AdminReservationFragment())
                R.id.admintip -> replaceFragment(TipFragment())
            }
            true
        }

        // Initialize logout button
        val logoutButton = findViewById<ImageView>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Start IntroActivity when logout button is clicked
            startActivity(Intent(this@MainActivity2, IntroActivity::class.java))
            finish() // Close MainActivity2
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.adminreservation -> {
                Toast.makeText(this, "Reservation clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.admintip -> {
                Toast.makeText(this, "Tip clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.adminhome -> {
                // Handle the back button click
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // Handle the back button click
        // You can add your custom logic here if needed
        super.onBackPressed()
    }
}
