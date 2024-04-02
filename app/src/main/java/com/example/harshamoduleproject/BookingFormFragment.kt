package com.example.harshamoduleproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class BookingFormFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firstNameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var roomTypeTextView: TextView
    private lateinit var checkinTextView: TextView
    private lateinit var roomNumberEditText: EditText
    private lateinit var daysEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking_form, container, false)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings")

        // Initialize TextViews
        firstNameTextView = view.findViewById(R.id.firstNameTextView)
        phoneTextView = view.findViewById(R.id.phoneTextView)
        roomTypeTextView = view.findViewById(R.id.roomTypeTextView)
        checkinTextView = view.findViewById(R.id.checkinTextView)

        // Initialize EditText fields
        roomNumberEditText = view.findViewById(R.id.roomNumberEditText)
        daysEditText = view.findViewById(R.id.daysEditText)

        // Fetch check-out bookings data
        val userId = arguments?.getString("userId")
        userId?.let {
            fetchCheckOutBookings(it)
        }

        // Set up submit button click listener
        val submitButton = view.findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener {
            val userId = arguments?.getString("userId")
        userId?.let {
            updateBooking(it)
        }
        }

        return view
    }

    private fun fetchCheckOutBookings(userId: String) {
        databaseReference.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (bookingSnapshot in snapshot.children) {
                        val addRoomData = bookingSnapshot.getValue(AddRoomData::class.java)
                        addRoomData?.let { addRoomData ->
                            // Populate TextViews with fetched data
                            firstNameTextView.text = addRoomData.firstname ?: ""
                            phoneTextView.text = addRoomData.phone ?: ""
                            roomTypeTextView.text = addRoomData.roomtype ?: ""
                            checkinTextView.text = addRoomData.checkIn ?: ""

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })
    }
f
    private fun updateBooking(userId: String) {
        val roomNumber = roomNumberEditText.text.toString().toIntOrNull()
        val days = daysEditText.text.toString().toIntOrNull()

        if (userId.isNotBlank() && roomNumber != null && days != null) {
            val bookingRef = databaseReference.orderByChild("userId").equalTo(userId)

            bookingRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (bookingSnapshot in snapshot.children) {
                        // Get the key of the booking
                        val bookingKey = bookingSnapshot.key
                        val addRoomData = bookingSnapshot.getValue(AddRoomData::class.java)

                        if (addRoomData != null && bookingKey != null) {
                            // Update the AddRoomData object with room number and days
                            addRoomData.roomno = roomNumber
                            addRoomData.days = days

                            // Update the booking data in the database
                            databaseReference.child(bookingKey).setValue(addRoomData)
                                .addOnSuccessListener {
                                    // Toast.makeText(requireContext(), "Booking updated successfully", Toast.LENGTH_SHORT).show()
                                    Toast.makeText(requireContext(), "Booking checked-in successfully", Toast.LENGTH_SHORT).show()
                                    // Redirect to CheckinFragment after successful booking
                                    redirectToCheckinFragment()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireContext(), "Error updating booking: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(requireContext(), "Booking data not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Invalid room number or days", Toast.LENGTH_SHORT).show()
        }
    }

    private fun redirectToCheckinFragment() {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, CheckInFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }



}
