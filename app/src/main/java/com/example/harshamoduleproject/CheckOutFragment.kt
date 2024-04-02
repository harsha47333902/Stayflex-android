package com.example.harshamoduleproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.firebase.database.*

class CheckOutFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tableLayout: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_check_out, container, false)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings")

        // Initialize table layout
        tableLayout = view.findViewById(R.id.tableLayout)

        // Fetch check-out bookings data
        fetchCheckOutBookings()

        return view
    }

    private fun fetchCheckOutBookings() {
        databaseReference.orderByChild("status").equalTo("check-out")
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

                            val roomnoTextView = TextView(context)
                            roomnoTextView.text = addRoomData.roomno.toString()

                            val priceTextView = TextView(context)
                            priceTextView.text = addRoomData.price.toString()
                            val chekoutTextView = TextView(context)
                            chekoutTextView.text = addRoomData.checkOut
                            // Add views to the table row
                            tableRow.addView(firstNameTextView)
                            tableRow.addView(adultTextView)
                            tableRow.addView(childTextView)
                            tableRow.addView(addressTextView)
                            tableRow.addView(phoneTextView)
                            tableRow.addView(roomTypeTextView)
                            tableRow.addView(roomnoTextView)
                            tableRow.addView(priceTextView)
                            tableRow.addView(chekoutTextView)
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
}
