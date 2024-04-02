package com.example.harshamoduleproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.*

class PendingFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tableLayout: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pending, container, false)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings")

        // Initialize table layout
        tableLayout = view.findViewById(R.id.tableLayout)

        // Fetch pending bookings data
        fetchPendingBookings()

        return view
    }

    private fun fetchPendingBookings() {
        databaseReference.orderByChild("status").equalTo("pending")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (bookingSnapshot in snapshot.children) {
                        val addRoomData = bookingSnapshot.getValue(AddRoomData::class.java)
                        addRoomData?.let { addRoomData ->
                            // Create a table row
                            val tableRow = TableRow(context)
                            val layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            tableRow.layoutParams = layoutParams

                            // Populate the table row with data
                            val firstNameTextView = TextView(context)
                            firstNameTextView.text = addRoomData.firstname

                            val adultTextView = TextView(context)
                            adultTextView.text = addRoomData.adult

                            val childTextView = TextView(context)
                            childTextView.text = addRoomData.child

                            val addressTextView = TextView(context)
                            addressTextView.text = addRoomData.address
                            val phoneTextView = TextView(context)
                            phoneTextView.text = addRoomData.phone
                            val statusTextView = TextView(context)
                            statusTextView.text = addRoomData.status
                            val roomTypeTextView = TextView(context)
                            roomTypeTextView.text = addRoomData.roomtype
                            val priceTextView = TextView(context)
                            priceTextView.text = addRoomData.price

                            // Add views to the table row
                            tableRow.addView(firstNameTextView)
                            tableRow.addView(adultTextView)
                            tableRow.addView(childTextView)
                            tableRow.addView(addressTextView)
                            tableRow.addView(phoneTextView)
                            tableRow.addView(roomTypeTextView)
                            tableRow.addView(priceTextView)
                            tableRow.addView(statusTextView)

                            // Add buttons for actions
                            val acceptButton = Button(context)
                            acceptButton.text = "Accept"
                            acceptButton.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                            acceptButton.setOnClickListener {
                                acceptBooking(bookingSnapshot.key, addRoomData.userId)
                                tableLayout.removeView(tableRow)
                            }
                            tableRow.addView(acceptButton)

                            val rejectButton = Button(context)
                            rejectButton.text = "Reject"
                            rejectButton.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                            rejectButton.setOnClickListener {
                                rejectBooking(bookingSnapshot.key)
                                tableLayout.removeView(tableRow)
                            }
                            tableRow.addView(rejectButton)

                            // Add the table row to the table layout
                            tableLayout.addView(tableRow)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })
    }

    private fun acceptBooking(bookingKey: String?, userId: String?) {
        if (bookingKey != null && userId != null) {
            databaseReference.child(bookingKey).child("status").setValue("check-in")
                .addOnSuccessListener {
                   // Toast.makeText(requireContext(), "Booking checked-in successfully", Toast.LENGTH_SHORT).show()
                    // Pass userId to BookingFormFragment
                    val bookingFormFragment = BookingFormFragment().apply {
                        arguments = Bundle().apply {
                            putString("userId", userId)
                        }
                    }
                    // Replace the current fragment with BookingFormFragment
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragmentContainer, bookingFormFragment)?.commit()
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }

    private fun rejectBooking(bookingKey: String?) {
        if (bookingKey != null) {
            databaseReference.child(bookingKey).child("status").setValue("rejected")
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Booking rejected", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }
}
