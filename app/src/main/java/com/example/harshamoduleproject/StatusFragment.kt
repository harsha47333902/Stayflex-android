package com.example.harshamoduleproject

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.firebase.database.*

class StatusFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tableLayout: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_status, container, false)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings")

        // Initialize table layout
        tableLayout = view.findViewById(R.id.tableLayout)

        // Fetch check-out bookings data
        val userId = arguments?.getString("userId")
        userId?.let {
            fetchCheckOutBookings(it)
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
                            // Create a table row
                            val tableRow = TableRow(context)
                            val layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            tableRow.layoutParams = layoutParams

                            // Populate the table row with data
                            val checkinTextView = TextView(context).apply {
                                text = addRoomData.checkIn
                                gravity = Gravity.CENTER
                            }
                            val roomTypeTextView = TextView(context).apply {
                                text = addRoomData.roomtype
                                gravity = Gravity.CENTER
                            }
                            val roomnoTextView = TextView(context).apply {
                                text = addRoomData.roomno.toString()
                                gravity = Gravity.CENTER
                            }
                            val statusTextView = TextView(context).apply {
                                text = addRoomData.status ?: "N/A" // Populate status or show "N/A" if not available
                                gravity = Gravity.CENTER
                            }

                            // Add views to the table row
                            tableRow.addView(checkinTextView)
                            tableRow.addView(roomTypeTextView)
                            tableRow.addView(roomnoTextView)
                            tableRow.addView(statusTextView)

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


//    private fun fetchCheckOutBookings(userId: String) {
//        databaseReference.orderByChild("userId").equalTo(userId)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (bookingSnapshot in snapshot.children) {
//                        val addRoomData = bookingSnapshot.getValue(AddRoomData::class.java)
//                        addRoomData?.let { addRoomData ->
//                            // Create a table row
//                            val tableRow = TableRow(context)
//                            val layoutParams = TableRow.LayoutParams(
//                                TableRow.LayoutParams.MATCH_PARENT,
//                                TableRow.LayoutParams.WRAP_CONTENT
//                            )
//                            tableRow.layoutParams = layoutParams
//
//                            // Populate the table row with data
//                            val firstNameTextView = TextView(context)
//                            firstNameTextView.text = addRoomData.firstname
//                            val checkinTextView = TextView(context)
//                            checkinTextView.text = addRoomData.checkIn
//                            val roomTypeTextView = TextView(context)
//                            roomTypeTextView.text = addRoomData.roomtype
//                            val priceTextView = TextView(context)
//                            priceTextView.text = addRoomData.price
//                            val roomnoTextView = TextView(context)
//                            roomnoTextView.text = addRoomData.roomno.toString()
//
//
//                            val statusTextView = TextView(context)
//                            statusTextView.text = addRoomData.status ?: "N/A" // Populate status or show "N/A" if not available
//
//                            // Add views to the table row
////                            tableRow.addView(firstNameTextView)
//                            tableRow.addView(checkinTextView)
//                            tableRow.addView(roomTypeTextView)
//                            tableRow.addView(roomnoTextView)
////                            tableRow.addView(priceTextView)
//                            tableRow.addView(statusTextView)
//
//                            // Add the table row to the table layout
//                            tableLayout.addView(tableRow)
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle errors
//                }
//            })
//    }
}
