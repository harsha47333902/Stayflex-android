package com.example.harshamoduleproject

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.firebase.database.*

class AdminHomeFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var addRoomButton: Button
    private lateinit var tableLayout: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Rooms")

        // Initialize table layout
        tableLayout = view.findViewById(R.id.tableLayout)

        // Initialize addRoomButton
        addRoomButton = view.findViewById(R.id.addRoomButton)

        // Fetch check-out bookings data
        fetchCheckOutBookings()

        // Set click listener for addRoomButton
        addRoomButton.setOnClickListener {
            startActivity(Intent(requireActivity(), AddRoomsActivity::class.java))
        }

        return view
    }

    private fun fetchCheckOutBookings() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (roomSnapshot in snapshot.children) {
                    val addRoomData = roomSnapshot.getValue(AddRoomData::class.java)
                    addRoomData?.let { addRoomData ->
                        // Create a table row
                        val tableRow = TableRow(context)
                        val layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        tableRow.layoutParams = layoutParams

                        // Populate the table row with data
                        val roomtypeTextView = TextView(context).apply {
                            text = addRoomData.roomtype
                            gravity = Gravity.CENTER
                        }
                        val priceTextView = TextView(context).apply {
                            text = addRoomData.price
                            gravity = Gravity.CENTER
                        }
                        val actionButton = Button(context).apply {
                            text = "Delete"
                            setOnClickListener {
                                // Call function to delete the room data
                                deleteRoom(roomSnapshot.key)
                                // Remove the table row from the table layout
                                tableLayout.removeView(tableRow)
                            }
                        }

                        // Add views to the table row
                        tableRow.addView(roomtypeTextView)
                        tableRow.addView(priceTextView)
                        tableRow.addView(actionButton)

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

    private fun deleteRoom(roomKey: String?) {
        roomKey?.let {
            databaseReference.child(it).removeValue()
                .addOnSuccessListener {
                    // Handle success
                    // You might want to refresh the UI here if needed
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }
    }
}
