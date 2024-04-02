package com.example.harshamoduleproject

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CostumerroomdetailsActivity : AppCompatActivity() {

    private lateinit var checkInDate:TextView
    private lateinit var checkOutDate:TextView
    private lateinit var adultPicker:EditText
    private lateinit var childPicker:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costumerroomdetails)

        // Initialize checkInDate and checkOutDate TextViews
        checkInDate = findViewById(R.id.checkInDate)
        checkOutDate = findViewById(R.id.checkOutDate)

        // Retrieve details from intent extras
        val imageUrl = intent.getStringExtra("image_url") ?: ""
        val roomImageView: ImageView = findViewById(R.id.roomImageView)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image while loading (optional)
            .error(R.drawable.ic_launcher_foreground) // Image to display if loading fails (optional)
            .centerCrop() // Crop the image to fit the ImageView
            .into(roomImageView)
        val roomType = intent.getStringExtra("room_type") ?: ""
        val price = intent.getStringExtra("price") ?: ""

        // Retrieve userId from intent extras
        val userId = intent.getStringExtra("userId") ?: ""

        // Initialize views
        val roomTypeTextView: TextView = findViewById(R.id.roomTypeTextView)
        val priceTextView: TextView = findViewById(R.id.priceTextView)

        checkInDate.setOnClickListener{
            showDatePicker()
        }
        checkOutDate.setOnClickListener{
            showDatePicker1()
        }

        // Set room type and price text
        roomTypeTextView.text = roomType ?: ""
        priceTextView.text = "Rs. $price" // Concatenate "Rs." with the price

        // Find the NumberPicker views
        adultPicker = findViewById(R.id.adultEditText)
        childPicker = findViewById(R.id.childEditText)

        // Set the minimum and maximum values for each NumberPicker
//        adultPicker.minValue = 1 // Minimum value
//        adultPicker.maxValue = 3 // Maximum value
//
//        childPicker.minValue = 0 // Minimum value
//        childPicker.maxValue = 2 // Maximum value

        // Initialize views
        val bookButton: Button = findViewById(R.id.book_button)

        // Set onClickListener for the "Book Now" button
        bookButton.setOnClickListener {
            // Check if all fields are completed
            if (isAllFieldsCompleted()) {
                // Retrieve details entered by the user
                val firstname = findViewById<EditText>(R.id.firstname).text.toString()
                val lastname = findViewById<EditText>(R.id.lastname).text.toString()
                val address = findViewById<EditText>(R.id.address).text.toString()
                val phone = findViewById<EditText>(R.id.phone).text.toString()
                val adult = adultPicker.text.toString() // Use value property to get selected value
                val child = childPicker.text.toString() // Use value property to get selected value
                val checkIn = checkInDate.text.toString() // Use value property to get selected value
                val checkOut = checkOutDate.text.toString() // Use value property to get selected value

                // Create an instance of AddRoomData
                val addRoomData = AddRoomData(
                    firstname = firstname,
                    lastname = lastname,
                    address = address,
                    phone = phone,
                    checkIn = checkIn,
                    checkOut = checkOut,
                    imageUrl = imageUrl,
                    roomtype = roomType,
                    price = price,
                    userId = userId,
                    adult = adult,
                    child = child
                )

                // Save booking information to the new database table
                saveBookingInfo(addRoomData)
            } else {
                // Display toast message if any field is empty
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun isAllFieldsCompleted(): Boolean {
        val firstname = findViewById<EditText>(R.id.firstname).text.toString()
        val lastname = findViewById<EditText>(R.id.lastname).text.toString()
        val address = findViewById<EditText>(R.id.address).text.toString()
        val phone = findViewById<EditText>(R.id.phone).text.toString()
        val adultCount = adultPicker.text.toString()
        val childCount = childPicker.text.toString()

//        val date = dateButton.text.toString()

        return firstname.isNotEmpty() && lastname.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty() && adultCount.isNotEmpty() && childCount.isNotEmpty()
    }


    private fun saveBookingInfo(addRoomData: AddRoomData) {
        // Reference to the new database table
        val bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings")

        // Push booking information to the database
        val bookingKey = bookingsRef.push().key
        if (bookingKey != null) {
            bookingsRef.child(bookingKey).setValue(addRoomData)
                .addOnSuccessListener {
                    Toast.makeText(this@CostumerroomdetailsActivity, "Successfully registered ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CostumerroomdetailsActivity, MainActivity::class.java))
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@CostumerroomdetailsActivity, "Booking unsuccessful", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun showDatePicker() {

        checkInDate = findViewById(R.id.checkInDate)


        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                checkInDate.text = date

            },  // Pass the current date as the default values
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    fun showDatePicker1() {


        checkOutDate = findViewById(R.id.checkOutDate)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                checkOutDate.text = date
            },  // Pass the current date as the default values
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
}
